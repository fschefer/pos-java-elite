# ☕ Java Elite - Pós Graduação

> **Repositório em Evolução Contínua**
> *Este projeto acompanha minha jornada na especialização em Java, integrando teoria profunda e prática avançada.*

Repositório central de estudos (**Knowledge Base**) e implementações práticas (**Labs**) da Pós-Graduação Java Elite da UNIPDS. O foco é dominar desde a engenharia profunda da linguagem até arquiteturas de microsserviços e IA.

---

## 🗺️ Roadmap & Status

| Módulo                         | Temas Principais                                | Status          |
| :----------------------------- | :---------------------------------------------- | :-------------- |
| **01. Fundamentos (Core)**     | OO, Collections, Streams, I/O, Exceptions       | ✅ **Concluído** |
| **01. Fundamentos (Advanced)** | Gradle, Deep Dive Collections, Networking, HTTP | ✅ **Concluído** |
| **02. Back-End + IA**          | Spring Boot, Quarkus, APIs REST, LangChain4j    |  *Em andamento* |
| **03. Front-End**              | React, Integração com APIs                      | 🔒 *Aguardando* |
| **04. Arquitetura**            | Clean Arch, Event-Driven, DDD, System Design    | 🔒 *Aguardando* |
| **05. Infra & Cloud**          | Docker, Kubernetes, AWS, CI/CD                  | 🔒 *Aguardando* |
| **06. Persistência**           | SQL, NoSQL (Redis/Mongo), JPA                   | 🔒 *Aguardando* |
| **07. Qualidade**              | TDD, Testes de Integração, Benchmarking         | 🔒 *Aguardando* |

---

## 📂 Estrutura do Monorepo

## Estrutura do Projeto

O repositório é híbrido, contendo notas e código:

- **`notas-de-aula/`**: Notas de estudo detalhadas (formato Obsidian).

- **`labs/java-fundamentos/`**: Código fonte prático do módulo Core (Maven).
  - `fundamentos.oo`: Abstração, Encapsulamento, Herança, Polimorfismo.
  - `fundamentos.colecoes`: Performance de List, Set e Map.
  - `fundamentos.apis`: Uso de Optional, Date/Time, Reflection e Regex.
  - `fundamentos.io`: Manipulação de Arquivos (IO vs NIO vs NIO.2).
  - `fundamentos.streams`: Processamento funcional de dados.
  - `fundamentos.excecoes`: Tratamento de erros e exceções de negócio.

- **`labs/java-advanced/`**: Código fonte avançado (Gradle).
  - `mx.florinda.cardapio`: Imutabilidade com Records, Enums e Deep Dive em Collections (`WeakHashMap`, `IdentityHashMap`).
  - `mx.florinda.networking`: Serialização JSON (Gson), Cliente HTTP (`java.net.http`), Servidores Web e Sockets TCP "na unha".

- **`labs/quarkus-intro/`**: Desenvolvimento Cloud Native e Microsserviços (Maven).
  - `APIs`: APIs RESTful imperativas e reativas (`UnipdsResource`) e integração via REST Client (`StarWarsService`).
  - `Resiliência`: Padrões de Tolerância a Falhas como Circuit Breaker, Timeout e Fallback aplicados em integrações externas (`StarWarsResource`).
  - `Observabilidade`: Monitoramento com Health Checks (`LivenessCheck`, `ReadinessCheck`) e Métricas de Negócio com Micrometer (`@Counted`).
  - `Persistência`: Persistência simplificada com Hibernate Panache (Active Record), Entidades (`Pessoa`) e Recursos Transacionais (`PessoaResource`).
  - `Segurança`: Implementação de Segurança com JWT e RBAC (`SecurityResource`), protegendo endpoints por papéis (`@RolesAllowed`)..
  
  - **`labs/springboot-intro/`** (Maven - Spring Boot 4 & Java 25):
  - **Domínio**: API REST para Gestão de Eventos e Inscrições.
  - **Modelagem ORM Avançada** (`br.com.unipds.evento.model`):
    - Entidades relacionais: `User`, `Conference`, `Session`.
    - Relacionamento N:N com atributos extras: Entidade associativa `Subscription` utilizando `@EmbeddedId` e chave composta `SubscriptionId`.
  - **Arquitetura em Camadas**:
    - `repository`: Interfaces `ListCrudRepository` para persistência (MySQL/H2).
    - `service`: Regras de negócio (geração de UUID, auditoria de datas) em `SubscriptionService` e orquestração em `SessionService`.
    - `controller`: Exposição de endpoints REST e injeção de dependências.
  - **Qualidade & Docs**:
    - Tratamento global de erros com `@ControllerAdvice` (`GlobalExceptionHandler`).
    - Documentação automática com **SpringDoc OpenAPI (Swagger)**.
    - Testes de Integração com `@WebMvcTest` e Mockito.
---

## 🛠️ Tecnologias & Stack

  <div align="left">
  <img src="https://img.shields.io/badge/Java-25%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Quarkus-4695EB?style=for-the-badge&logo=quarkus&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot_4-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/GraalVM-E95420?style=for-the-badge&logo=graalvm&logoColor=white" />
  <br />
  
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/OpenTelemetry-000000?style=for-the-badge&logo=opentelemetry&logoColor=white" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
  <br />
  
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Mockito-788BD2?style=for-the-badge&logo=mockito&logoColor=white" />
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white" />
  <img src="https://img.shields.io/badge/Arch_Linux-1793D1?style=for-the-badge&logo=arch-linux&logoColor=white" />
</div>
---
*Desenvolvido por Flávio Schefer.*
