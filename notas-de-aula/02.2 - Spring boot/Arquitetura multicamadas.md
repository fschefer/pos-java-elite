## Documentação Automática (SpringDoc / Swagger)

### 1.1 O Problema

Ao desenvolver APIs, muitas vezes o consumidor (Front-end, Mobile, parceiros) não tem acesso ao código fonte ou permissão para instalar ferramentas como o Postman. É necessário uma forma padronizada e acessível de expor os endpoints.

### 1.2 A Solução: OpenAPI (Swagger)

Utilizamos a biblioteca **SpringDoc**. Ela lê as anotações do Spring (`@RestController`, `@GetMapping`, etc.) e gera automaticamente uma página HTML interativa.

- **Vantagem:** Permite testar a API ("Try it out") diretamente pelo navegador, sem instalar nada.
    
- **Acesso:** Geralmente em `http://localhost:8080/swagger-ui.html`.
    

---

## 2. Arquitetura de Software (MVC e Camadas)

Para evitar "código espaguete", o Spring Boot utiliza uma arquitetura baseada em camadas com responsabilidades únicas (Single Responsibility Principle).

### 2.1 As Camadas

1. **Controller:** A "porta de entrada". Expõe os endpoints (URLs), recebe o JSON, delega para o Service e devolve o Status Code HTTP. **Não deve ter regra de negócio**.
    
2. **Service:** O "coração" da aplicação. Contém as regras de negócio (ex: cálculos, validações complexas, orquestração). Implementa os Casos de Uso.
    
3. **Repository:** A camada de acesso a dados. Responsável apenas por falar com o Banco de Dados (SQL/NoSQL) e converter tabelas em objetos Java.
    
4. **Model/Entity:** As classes que representam as tabelas do banco.
    
5. **DTO (Data Transfer Object):** Objetos simples para transportar dados entre camadas, filtrar o que é enviado para o cliente ou receber dados de formulários.
    

---

## 3. Desacoplamento e Injeção de Dependência

### 3.1 Alta Coesão e Baixo Acoplamento

- **Alta Coesão:** A classe faz bem uma única coisa.
    
- **Baixo Acoplamento:** As classes dependem pouco umas das outras, facilitando trocas e testes.
    

### 3.2 O Padrão Interface-Service

Para atingir baixo acoplamento, o Controller não deve depender de uma classe concreta (`ServiceV1`), mas sim de uma **Interface** (`IService`).

- Isso permite trocar a implementação da regra de negócio (ex: mudar a regra de cálculo de frete) sem mexer em uma linha de código do Controller.
    

### 3.3 @Qualifier

Quando temos uma Interface e duas classes que a implementam (ex: `ServicoV1` e `ServicoV2`), o Spring não sabe qual injetar. Usamos a anotação `@Qualifier("nome")` para decidir qual implementação usar em tempo de execução.