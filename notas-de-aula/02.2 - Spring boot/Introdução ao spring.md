## 1. Filosofia e Arquitetura

### O Ecossistema Spring vs. Spring Boot

Muitos confundem os dois, mas a distinção é crucial para a engenharia de software:

- **Spring Framework (O Motor):** É um framework de inversão de controle (IoC) e injeção de dependência. Ele fornece a infraestrutura básica (gerenciamento de transações, segurança, acesso a dados), mas historicamente exigia uma configuração XML verbosa ou classes `@Configuration` complexas.
    
- **Spring Boot (O Acelerador):** É uma ferramenta _opinionated_ (opinativa). Ele toma decisões de arquitetura por si (convenção sobre configuração) para que o programador não precise fazê-lo.
    
    - **Auto-Configuration:** Ele escaneia o _classpath_. Se encontrar a biblioteca `h2.jar`, ele configura automaticamente um banco em memória. Se encontrar `spring-webmvc.jar`, ele configura o DispatcherServlet e o Tomcat.
        
    - **Standalone:** O objetivo é criar aplicações que "apenas rodam" (`java -jar`), sem depender de um servidor de aplicação externo (como WildFly ou WebLogic).
        

### Biblioteca vs. Framework vs. API

A aula estabelece uma distinção teórica importante para arquitetos de software:

1. **Biblioteca (Library):** Código passivo. Você tem o controle e chama os métodos quando precisa (ex: Math, Gson).
    
2. **API (Application Programming Interface):** Contrato de comunicação. Define _como_ dois sistemas interagem, abstraindo a implementação (ex: JDBC, API REST).
    
3. **Framework:** Inversão de Controle (IoC). O framework chama o seu código (Hollywood Principle: "Não nos ligue, nós ligamos para você"). Ele dita a estrutura e o ciclo de vida (ex: O Spring instancia seu Controller, não você).
    

---

## 2. Anatomia de uma Aplicação Spring Boot

### O Maven e o `pom.xml`

A estrutura do projeto é gerida pelo Maven (ou Gradle), que utiliza o conceito de **Starters**.

- **Starters:** São descritores de dependência que agrupam bibliotecas comuns.
    
    - `spring-boot-starter-web`: Traz consigo o Spring MVC, Jackson (JSON), Logback (Logging) e o Tomcat Embutido.
        
    - `spring-boot-starter-test`: Traz JUnit 5, Mockito, AssertJ e Hamcrest.
        
- **Parent POM:** O projeto geralmente herda de `spring-boot-starter-parent`, que gerencia as versões das dependências para evitar conflitos (Dependency Hell).
    

### A Classe Principal (`@SpringBootApplication`)

Esta anotação é, na verdade, uma "meta-anotação" que combina três outras essenciais:

1. **`@Configuration`**: Permite registrar beans extras no contexto ou importar outras classes de configuração.
    
2. **`@EnableAutoConfiguration`**: Habilita o mecanismo "mágico" do Boot de configurar beans baseados no classpath.
    
3. **`@ComponentScan`**: Instrui o Spring a procurar componentes (`@Component`, `@Service`, `@Controller`) no pacote atual e em seus subpacotes.
    
    - _Atenção Crítica:_ Classes definidas em pacotes "acima" da classe main não serão encontradas, gerando erros 404 ou `NullPointerException`.
        

---

## 3. Desenvolvimento RESTful e Camada Web

### Ciclo de Vida da Requisição (Simplificado)

1. **Tomcat Embutido:** Escuta na porta 8080 (padrão).
    
2. **DispatcherServlet:** Recebe a requisição HTTP.
    
3. **HandlerMapping:** Identifica qual método de qual `@RestController` deve processar a rota (ex: `/produtos`).
    
4. **Jackson (Serialização):**
    
    - **Entrada (`@RequestBody`):** O Jackson desserializa o JSON do corpo da requisição para um Objeto Java (binding).
        
    - **Saída (`@ResponseBody`):** O retorno do método é serializado de Objeto Java para JSON. A anotação `@RestController` já inclui `@ResponseBody` implicitamente em todos os métodos.
        

### Verbos HTTP e Semântica

Um endpoint não é apenas uma URL, é a combinação de **URL + Verbo HTTP**:

- **GET** (Idempotente): Recuperação de dados. Não deve ter efeitos colaterais.
    
- **POST** (Não-idempotente): Criação de recursos. Geralmente retorna 201 (Created).
    
- **PUT**: Atualização total (substitui o recurso inteiro).
    
- **PATCH**: Atualização parcial (modifica apenas campos específicos).
    
- **DELETE**: Remoção de recursos.
    

---

## 4. Ferramentas de Produtividade

### Spring DevTools

Habilita o **Hot Reload** (reinicialização rápida). Diferente de um restart completo da JVM, o DevTools usa dois ClassLoaders:

1. **Base ClassLoader:** Carrega bibliotecas (JARs) que não mudam.
    
2. **Restart ClassLoader:** Carrega as classes do seu projeto.
    
    Quando você salva um arquivo, apenas o Restart ClassLoader é descartado e recriado, tornando o processo muito mais rápido que um "Cold Start".
    

### Spring Initializr (start.spring.io)

É a ferramenta padrão (SaaS) para bootstrap de projetos. Ela garante que você receba um `pom.xml` compatível e a estrutura de pastas correta (`src/main/java`, `src/main/resources`), evitando erros manuais de configuração inicial.

---

## 5. Resumo de Anotações Críticas

| **Anotação**      | **Função Técnica**                                                                                                                            | **Contexto** |
| ----------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ------------ |
| `@RestController` | Combina `@Controller` e `@ResponseBody`. Diz ao Spring que esta classe manipula requisições web e retorna dados (JSON/XML) diretamente.       | Classe       |
| `@GetMapping`     | Abreviação para `@RequestMapping(method = RequestMethod.GET)`.                                                                                | Método       |
| `@PostMapping`    | Abreviação para `@RequestMapping(method = RequestMethod.POST)`.                                                                               | Método       |
| `@RequestBody`    | Aciona o `HttpMessageConverter` (Jackson) para converter o corpo da requisição (JSON) em Objeto Java. Sem isso, o objeto chega nulo ou vazio. | Parâmetro    |
| `@PathVariable`   | Extrai valores da URL (ex: `/produtos/{id}`).                                                                                                 | Parâmetro    |
| `@RequestParam`   | Extrai valores da Query String (ex: `/produtos?nome=mouse`).                                                                                  | Parâmetro    |