	
**REST (Representational State Transfer)** é um estilo arquitetural para sistemas distribuídos. Uma API REST é a porta de entrada da aplicação para o mundo externo (Apps, Front-end, outros serviços).

### Métodos HTTP e CRUD

Um **Endpoint** é a combinação de uma URL + Método HTTP.

- **GET:** Recuperar dados (Read).
    
- **POST:** Criar novos dados (Create).
    
- **PUT:** Atualizar dados completamente (Update).
    
- **DELETE:** Remover dados (Delete).
    

---

## 2. Quarkus REST (Jakarta REST)

O Quarkus utiliza a especificação **Jakarta REST** (antigo JAX-RS).

- **Histórico:** O Java EE virou Jakarta EE (gerido pela Eclipse Foundation) por questões de marca da Oracle. Por isso, os pacotes mudaram de `javax.*` para `jakarta.*`.
    
- **Motor:** A implementação do Quarkus (Quarkus REST) roda sobre o **Vert.x**, o que permite alta performance e suporte a modelos bloqueantes (imperativos) e não-bloqueantes (reativos) simultaneamente.
    

### Anotações Principais

- `@Path("/caminho")`: Define a URI do recurso.
    
- `@GET`, `@POST`, `@DELETE`, `@PUT`: Define o verbo HTTP.
    
- `@Produces(MediaType.APPLICATION_JSON)`: Define o formato da resposta (saída).
    
- `@Consumes(MediaType.TEXT_PLAIN)`: Define o formato aceito na requisição (entrada).
    

> **Dica:** É uma boa prática definir `@Produces` e `@Consumes` no nível da **Classe** se todos os métodos seguirem o mesmo padrão.

---

## 3. Consumindo APIs Externas (REST Client)

Para que sua aplicação acesse serviços remotos (ex: API do Star Wars, Google, etc.), usa-se o padrão **REST Client** (do MicroProfile).

1. **Adicionar Extensão:** `mvn quarkus:add-extension -Dextensions="quarkus-rest-client"`.
    
2. **Interface:** Cria-se uma interface (ex: `StarWarsService`) que espelha o serviço remoto.
    
3. **Anotação:** `@RegisterRestClient(baseUri = "...")` define a URL base.
    
4. **Injeção:** Usa-se `@Inject` e `@RestClient` para injetar essa interface no seu Resource local.