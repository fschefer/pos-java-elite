## 1. O que é o Panache?

O **Hibernate ORM com Panache** é uma camada de abstração criada especificamente para o Quarkus com o objetivo de simplificar o desenvolvimento da camada de persistência.

- **Problema:** O Hibernate/JPA tradicional é extremamente poderoso, mas verboso para casos de uso simples (CRUDs), exigindo muita configuração e código repetitivo (Boilerplate).
    
- **Solução:** O Panache adota o padrão **Active Record** (embora suporte o padrão Repository também), onde a própria entidade possui métodos estáticos para acessar o banco de dados.
    

---

## 2. Configuração Básica

Para começar, adicionamos as extensões necessárias:

- `quarkus-hibernate-orm-panache`: O núcleo do Panache.
    
- `quarkus-jdbc-h2`: Driver para banco H2 (em memória).
    
- `quarkus-rest-jackson` (ou JSON-B): Para serializar as entidades em JSON automaticamente na API.
---
## 3. Criando uma Entidade com Panache

A entidade estende `PanacheEntity`, o que já fornece um ID (Long) e todos os métodos de persistência (`persist`, `listAll`, `findById`, etc.).

- **Campos Públicos:** O Panache incentiva o uso de campos públicos para reduzir o ruído visual. Em tempo de build, o Quarkus substitui o acesso direto ao campo por chamadas aos getters/setters (mesmo que gerados internamente).

---
O uso do Panache torna o Resource extremamente enxuto, pois não é necessário injetar Repositories ou criar DAOs.

O Quarkus executa automaticamente o arquivo `src/main/resources/import.sql` ao iniciar (no modo dev/test) se o Hibernate estiver configurado para `drop-and-create`.