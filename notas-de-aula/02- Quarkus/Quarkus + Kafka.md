
## 1. Objetivo da Aula
Implementar a leitura assíncrona de mensagens no microsserviço de **Pedidos**.
* **Fluxo:** O serviço de Pagamentos (Produtor) envia um evento `PagamentoConfirmado`.
* **Ação:** O serviço de Pedidos (Consumidor) lê este evento e atualiza o status do pedido correspondente no banco de dados de "REALIZADO" para "PAGO".

## 2. Configuração do Ambiente
* **Extensão Necessária:** `quarkus-smallrye-reactive-messaging-kafka`.
* **Configuração (`application.properties`):**
    * Definir o endereço do Broker (`kafka.bootstrap.servers`).
    * Desativar o *Dev Services* do Kafka (pois já estamos rodando um via Docker Compose).
    * Mapear o canal de entrada (`mp.messaging.incoming`) para o tópico correto.

## 3. Estrutura do Consumidor (Consumer)
O Quarkus utiliza o padrão **MicroProfile Reactive Messaging**.

* **Anotação `@Incoming("nome-do-canal")`:** Define que o método deve ser acionado a cada nova mensagem recebida no canal especificado.
* **Deserialização:** O Quarkus (com Jackson) converte automaticamente o JSON da mensagem para o objeto Java (DTO) definido no parâmetro do método.
* **Persistência Reativa:** Como o projeto usa *Hibernate Reactive with Panache*, a atualização do banco deve ser feita dentro de uma transação reativa (`Panache.withTransaction`).

## 4. Comandos Úteis
* **Adicionar extensão via CLI:**
  `quarkus ext add quarkus-smallrye-reactive-messaging-kafka`
* **Rodar em modo Dev:**
  `quarkus dev` (ou via plugin do Maven/IDE).

---

## 5. Destaques da Implementação
* **DTO (`PagamentoConfirmadoEvent`):** Classe simples (POJO/Record) para mapear os dados do evento (`pagamentoId`, `pedidoId`).
* **Lógica Reativa:** Uso de `Uni<Void>` e métodos como `.onItem().ifNotNull()` para manipular o fluxo de dados sem bloquear a thread.



---

## 1. Objetivo da Aula
Configurar o microsserviço de **Pagamentos** para atuar como **Produtor** de mensagens no Kafka.
* **Cenário:** Quando um pagamento é confirmado via API REST (`PUT /pagamentos/{id}`), o sistema deve emitir um evento `PagamentoConfirmadoEvent` para o tópico do Kafka.
* **Consumidores:** O serviço de Pedidos (já implementado) e o serviço de Nota Fiscal reagirão a este evento.

## 2. Configuração do Produtor (`application.properties`)
* **Extensão:** `quarkus-smallrye-reactive-messaging-kafka`.
* **Broker:** `kafka.bootstrap.servers=localhost:9094` (Porta externa definida no Docker Compose).
* **Canal de Saída (`Outgoing`):**
    * Nome do canal interno: `pagamentos-confirmados`.
    * Tópico no Kafka: `pagamentos-confirmados`.
    * Serializador: Jackson (JSON) automático.

## 3. Formas de Produzir Mensagens
O Quarkus oferece duas abordagens principais:

### A. Emitter (Imperativo) - *Utilizado na aula*
Injeta-se um objeto `Emitter<T>` e chama-se o método `.send(payload)`. É ideal para integrar código imperativo (como um Controller REST tradicional) com mensageria reativa.
* **Anotação:** `@Channel("nome-do-canal")`.

### B. @Outgoing (Reativo/Stream)
Anota-se um método que retorna um fluxo de dados (`Multi<T>`). O Quarkus consome esse fluxo e envia cada item para o Kafka automaticamente.

---

## 4. DTO do Evento (`PagamentoConfirmadoEvent`)
Classe simples (POJO ou Record) que representa o contrato da mensagem.
* **Campos:** `pagamentoId`, `pedidoId`, `valor`.
* **Importante:** Deve ter a mesma estrutura esperada pelos consumidores.

## 5. Fluxo de Execução
1. Cliente chama `PUT /pagamentos/{id}`.
2. `PagamentoResource` altera o status no banco (MySQL) para `CONFIRMADO`.
3. `PagamentoResource` cria o DTO do evento.
4. `Emitter.send(evento)` publica a mensagem no Kafka.
5. O Kafka distribui a mensagem para os grupos de consumidores (Pedidos, Nota Fiscal, Console).