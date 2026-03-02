# ☕ Java Elite - Pós Graduação

> **Repositório em Evolução Contínua**
> *Este projeto acompanha minha jornada na especialização em Java, integrando teoria profunda e prática avançada.*

Repositório central de estudos (**Knowledge Base**) e implementações práticas (**Labs**) da Pós-Graduação Java Elite da UNIPDS. O foco é dominar desde a engenharia profunda da linguagem até arquiteturas de microsserviços e IA.

---

## 🗺️ Roadmap & Status

| Módulo                         | Temas Principais                                | Status          |
| :----------------------------- | :---------------------------------------------- | :-------------- |
| **01. Fundamentos (Core)** | OO, Collections, Streams, I/O, Exceptions       | ✅ **Concluído** |
| **01. Fundamentos (Advanced)** | Gradle, Deep Dive Collections, Networking, HTTP | ✅ **Concluído** |
| **02. Back-End** | Spring Boot, Quarkus, APIs REST    | ✅ **Concluído**|
| **03. Arquitetura** | Clean Arch, Event-Driven, DDD, System Design    | 🔒 *Aguardando* |
| **04. Infra & Cloud** | Docker, Kubernetes, AWS, CI/CD                  | 🔒 *Aguardando* |
| **05. Persistência** | SQL, NoSQL (Redis/Mongo), JPA                   | 🔒 *Aguardando* |
| **06. Qualidade** | TDD, Testes de Integração, Benchmarking         | 🔒 *Aguardando* |

---

## 📂 Estrutura do Monorepo

O repositório é híbrido, contendo notas teóricas e laboratórios de código prático:

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
  - `Segurança`: Implementação de Segurança com JWT e RBAC (`SecurityResource`), protegendo endpoints por papéis (`@RolesAllowed`).
  
- **`labs/springboot-intro/`**: Introdução e Arquitetura Base (Maven).
  - **Domínio**: API REST para Gestão de Eventos e Inscrições.
  - **Modelagem ORM Avançada**: Relacionamentos N:N com atributos extras (`@EmbeddedId` e classe associativa `Subscription`).
  - **Arquitetura em Camadas**: Repositórios (`ListCrudRepository`), Serviços e Controladores.
  - **Qualidade & Docs**: Tratamento global de erros (`@ControllerAdvice`), Documentação (SpringDoc OpenAPI/Swagger) e Testes com `@WebMvcTest`.

- **`labs/bank-transfer/`**: Gestão Transacional e Propriedades ACID (Maven).
  - **Domínio**: Sistema de Transferência Bancária entre Contas.
  - **Controle Transacional**: Uso da anotação `@Transactional` para garantir a integridade dos dados (Atomicidade, Consistência, Isolamento e Durabilidade).
  - **Rollback Automático**: Simulação de reversão de dados em falhas de negócio (ex: saldo insuficiente).
  - **Persistência Física**: Configuração do H2 em modo arquivo (`file`) para testar a Durabilidade.

- **`labs/auth-api/`**: Segurança, Autenticação e Autorização (Maven).
  - **Domínio**: API Stateless Segura com Spring Security.
  - **Criptografia**: Proteção de senhas na base de dados utilizando o algoritmo `BCryptPasswordEncoder`.
  - **JWT (JSON Web Token)**: Geração, assinatura e decodificação de tokens utilizando a biblioteca `JJWT`.
  - **Filtros Customizados**: Interceção de requisições com `OncePerRequestFilter` e injeção de permissões no `SecurityContextHolder`.

- **`labs/reactive.config/`**: Programação Reativa e Integrações Assíncronas (Maven).
  - **Domínio**: API de Autorização de Documentos Fiscais.
  - **Spring WebFlux**: Transição do modelo bloqueante para o modelo reativo utilizando `Mono` e `Flux`.
  - **WebClient**: Consumo assíncrono e não-bloqueante de APIs externas com alta latência (Slow APIs).
  - **Testes Reativos**: Validação fluida de fluxos com o `WebTestClient` e `@WebFluxTest`.

---

## 🛠️ Tecnologias & Stack

<div align="left">
  <img src="https://img.shields.io/badge/Java-21%20/%2025%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot_3.4+-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_WebFlux-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/Quarkus-4695EB?style=for-the-badge&logo=quarkus&logoColor=white" />
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/H2_Database-2A475E?style=for-the-badge&logo=sqlite&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/OpenTelemetry-000000?style=for-the-badge&logo=opentelemetry&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Mockito-788BD2?style=for-the-badge&logo=mockito&logoColor=white" />
  <img src="https://img.shields.io/badge/Arch_Linux-1793D1?style=for-the-badge&logo=arch-linux&logoColor=white" />
</div>---
Desenvolvido por Flávio Schefer.
