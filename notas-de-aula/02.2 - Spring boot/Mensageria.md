
## 1. Síncrono vs. Assíncrono

### Comunicação Síncrona (Ex: Telefone / REST)
* **Conceito:** O cliente faz uma requisição e fica bloqueado aguardando a resposta imediata.
* **Dependência Temporal:** Cliente e Servidor precisam estar online simultaneamente.
* **Problemas:**
    * **Disponibilidade:** Se o serviço B cai, o serviço A (chamador) falha ou fica lento.
    * **Escalabilidade:** Um pico de acesso no serviço A sobrecarrega diretamente o serviço B.
* **Exemplos:** REST, SOAP, gRPC.

### Comunicação Assíncrona (Ex: WhatsApp / Messaging)
* **Conceito:** "Fire and forget". O produtor envia a mensagem para um intermediário (Broker) e segue o processamento. A resposta não é imediata.
* **Middleware:** Utiliza um *Message Oriented Middleware* (MOM) como Kafka, RabbitMQ, SQS.
* **Vantagens:**
    * **Desacoplamento:** O produtor não precisa conhecer quem são os consumidores.
    * **Disponibilidade:** Se o consumidor cair, a mensagem fica guardada no Broker até ele voltar.
    * **Escalabilidade Horizontal:** É possível adicionar mais instâncias de consumidores para processar a fila mais rápido.

---

## 2. Semântica da Mensagem

O que trafega no canal? Segundo *Vaughn Vernon* (DDD):

1.  **Documento:** Apenas transferência de dados (estado). Ex: JSON de um Cliente.
2.  **Comando:** Uma ordem imperativa para executar uma ação. Ex: `PagarPedido`.
3.  **Evento de Domínio (Domain Event):**
    * Notificação de que algo relevante aconteceu no passado.
    * **Verbo no Passado:** `PedidoPago`, `NotaFiscalEmitida`.
    * Contém: O objeto que mudou, o que aconteceu e o Timestamp.

---

## 3. Padrões de Mensageria

### A. Fila (Queue) - Ponto a Ponto
* **Arquitetura:** 1 Produtor -> 1 Fila -> N Consumidores.
* **Comportamento:** **Competição**. A mensagem é entregue a apenas **um** consumidor disponível.
* **Uso:** Balanceamento de carga (Load Balancing) entre várias instâncias do mesmo serviço (ex: escalar o serviço de Pedidos).

### B. Tópico (Topic) - Publicar/Assinar (Pub/Sub)
* **Arquitetura:** 1 Publicador -> 1 Tópico -> N Assinantes (Grupos distintos).
* **Comportamento:** **Broadcast**. A mesma mensagem é entregue para **todos** os serviços interessados.
* **Uso:** Desacoplamento total.
    * *Cenário:* O serviço de Pagamentos publica `PagamentoConfirmado`.
    * *Consumidor 1 (Pedidos):* Atualiza status para "Em Preparação".
    * *Consumidor 2 (Fiscal):* Emite Nota Fiscal.
    * *Consumidor 3 (Anti-fraude):* Analisa padrão de compra.


---

## 1. Conceitos Fundamentais do Kafka

### A. Tópicos e Partições
* **Tópico:** Canal onde as mensagens são publicadas.
* **Partições:** Subdivisões de um tópico que permitem escalabilidade e paralelismo.
    * *Exemplo:* Tópico `pagamentos-confirmados` com 2 partições (`0` e `1`).
* **Log Ordenado:** O Kafka armazena as mensagens em disco de forma sequencial e imutável (Append Only Log).

### B. Chaves (Keys) e Roteamento
* Quando um produtor envia uma mensagem **com chave**, o Kafka garante que todas as mensagens com a mesma chave vão sempre para a **mesma partição**.
    * *Cenário:* Chave = `id_pagamento`. Isso garante que eventos do mesmo pagamento sejam processados na ordem correta pelo mesmo consumidor.
* Se a mensagem for enviada **sem chave**, o Kafka distribui (Round-Robin) entre as partições.

### C. Grupos de Consumidores (Consumer Groups)
É o mecanismo que define se o consumo será **Competitivo (Fila)** ou **Broadcast (Tópico)**.

1.  **Múltiplas Instâncias no MESMO Grupo:**
    * O Kafka divide as partições entre as instâncias.
    * Cada mensagem é entregue a apenas **um** consumidor do grupo.
    * *Uso:* Escalar o processamento (Load Balancing).
    * *Nota:* Se houver mais consumidores do que partições, os consumidores excedentes ficam ociosos (standby).

2.  **Instâncias em Grupos DIFERENTES:**
    * Cada grupo recebe uma cópia de todas as mensagens.
    * *Uso:* Sistemas distintos reagindo ao mesmo evento (ex: Pedidos e Nota Fiscal).

---

## 2. Comandos Práticos (CLI)

Todos os comandos são executados dentro do container do Kafka.

### A. Criar Tópico
```bash
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic pagamentos-confirmados --partitions 2


### . Listar e Descrever Tópicos



```
kafka-topics.sh --bootstrap-server localhost:9092 --list
kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic pagamentos-confirmados
```

### C. Produzir Mensagens (Console Producer)

Envia mensagens com chave e valor (`key:value`).

Bash

```
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic pagamentos-confirmados --property "parse.key=true" --property "key.separator=;"
> 1;{"pagamentoId":1, "pedidoId":1}
> 2;{"pagamentoId":2, "pedidoId":2}
```

### D. Consumir Mensagens (Console Consumer)

- **--from-beginning:** Lê todas as mensagens persistidas no log (histórico), não apenas as novas.
    
- **--group:** Define o grupo de consumidores para testar a competição.
    

Bash

```
# Consumidor 1 (Grupo Teste)
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic pagamentos-confirmados --group grupo-teste --from-beginning

# Consumidor 2 (Grupo Teste - em outra aba)
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic pagamentos-confirmados --group grupo-teste
```

_Resultado:_ As mensagens (ex: chaves 4 e 5) serão divididas entre os dois consumidores, pois cada um assume uma partição diferente.

### E. Inspecionar Grupos de Consumidores

Verifica como as partições estão distribuídas e o "Lag" (atraso) de processamento.

Bash

```
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group grupo-teste
```

````

---

# 💻 Parte 2: Docker Compose (`docker-compose.yml`)

Este é o ficheiro utilizado na aula para subir o ambiente Kafka localmente.

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181

  kafka:
    image: confluentinc/cp-kafka:latest
    container_name: florinda-eats-kafka
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
      - 9092:9092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
````