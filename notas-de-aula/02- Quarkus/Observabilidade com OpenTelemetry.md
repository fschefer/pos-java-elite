## O que é Observabilidade?

Em arquiteturas de microsserviços e sistemas distribuídos, a observabilidade é a capacidade de entender o estado interno do sistema com base nos dados que ele gera externamente. Ela é essencial para identificar gargalos de performance e diagnosticar falhas, baseando-se em três pilares: **Logs, Métricas e Traces**.

### OpenTelemetry (OTel)

É o principal projeto da CNCF (_Cloud Native Computing Foundation_) para observabilidade. Ele padroniza a coleta e exportação de dados de telemetria (logs, métricas e traces). O Quarkus possui integração nativa, instrumentando a aplicação de forma automática.

---

## 2. Rastreamento Distribuído (Distributed Tracing)

O rastreamento permite visualizar o caminho completo de uma requisição enquanto ela viaja por diferentes serviços.

- **Extensão:** `quarkus-opentelemetry`.
    
- **Visualização (Jaeger):** O Jaeger é a ferramenta utilizada para coletar e visualizar os traces gerados pelo OTel. Ele exibe detalhes como duração, headers e erros em cada etapa (span).
    
- **Tracing de Banco de Dados (JDBC):** Por padrão, o OTel rastreia chamadas HTTP. Para ver as queries SQL dentro do trace, é necessário adicionar a biblioteca `opentelemetry-jdbc` e habilitar a telemetria do datasource.
    

---

## 3. Métricas com Micrometer

Enquanto o tracing foca no "onde" e "quando" de uma requisição específica, as métricas monitoram a saúde geral e tendências (ex: uso de CPU, memória, taxa de erro). O Quarkus adotou o **Micrometer** como padrão de mercado.

- **Formato Prometheus:** A extensão `quarkus-micrometer-registry-prometheus` expõe os dados no endpoint `/q/metrics` em um formato que o Prometheus consegue ler (scraping).
    
- **Métricas Personalizadas:** É possível criar contadores, gauges e timers customizados usando a anotação `@Counted` ou injetando o `MeterRegistry`.
    

