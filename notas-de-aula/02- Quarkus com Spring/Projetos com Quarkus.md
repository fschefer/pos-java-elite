A maneira mais eficiente de iniciar um projeto é através do portal **[code.quarkus.io](https://code.quarkus.io)**.

- **Gerenciadores de Build:** Suporte nativo para **Maven** e **Gradle**.
    
- **Linguagens:** Além do Java, é possível utilizar **Kotlin** e **Scala**.
    
- **Versões:** O portal oferece versões de ponta e versões **LTS** (como 3.15 ou 3.20) para estabilidade.
    
- **Java:** Embora validado no Java 21, o Quarkus é compatível com versões mais recentes, como o Java 24, operando em nível de JVM.
    

## 2. O Conceito de Extensões

As extensões no Quarkus vão muito além de simples dependências do Maven (`pom.xml`).

- **Funcionalidade:** Elas injetam código, criam classes de exemplo, adicionam configurações automáticas e otimizam o bytecode.
    
- **Categorias:** Estão agrupadas por finalidade: REST, Banco de Dados (JDBC), Event-Driven (Kafka), Observabilidade, Segurança e IA (LangChain4j).
    
- **Compatibilidade com Spring:** O Quarkus reimplementou as APIs do Spring (DI, Web, Security, Data) de forma otimizada. Isso permite que desenvolvedores Spring migrem para o Quarkus mantendo o conhecimento prévio, mas com performance superior.
    

##  3. Experiência de Desenvolvimento (DevX)

O Quarkus é reconhecido por um ecossistema focado na velocidade do desenvolvedor.

- **Developer Mode:** Ativado via `mvn quarkus:dev`. Permite o **Live Coding**, onde alterações no código são refletidas instantaneamente (geralmente em milissegundos) sem restart manual.
    
- **Dev UI:** Uma interface web acessível em tempo de desenvolvimento que centraliza informações de extensões, endpoints, documentação Swagger e métricas.
    
- **Dev Services:** Se você configurar uma extensão (como PostgreSQL) mas não fornecer credenciais, o Quarkus sobe automaticamente um container via Docker (Testcontainers) e conecta sua aplicação a ele.
    
- **Terminal Interativo:** Permite executar testes e forçar reinicializações diretamente pelo terminal.
    

##  4. Compilação Nativa e Performance

O Quarkus gera bytecode otimizado desde a origem, o que potencializa o uso da **GraalVM** para gerar executáveis nativos.

- **Vantagem:** O executável nativo embarca tudo o que é necessário para rodar, eliminando a necessidade de uma JVM instalada no servidor de destino.
    
- **Startup:** Aplicações nativas podem iniciar em tempos extremamente baixos, como **28 milissegundos**.
    

---