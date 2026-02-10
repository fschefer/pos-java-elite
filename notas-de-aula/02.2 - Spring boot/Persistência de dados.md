
### 1.1 O Ecossistema Spring Data

Para persistir dados, utilizamos uma pilha de tecnologias que abstrai a complexidade do JDBC puro:
* **Spring Data:** Uma abstração de alto nível que padroniza o acesso a dados, seja SQL (JPA) ou NoSQL (Mongo, Redis).
* **JPA (Jakarta Persistence API):** A especificação (contrato) que define como objetos Java são mapeados para bancos relacionais (ORM - Object Relational Mapping).
* **Hibernate:** A implementação mais famosa e madura da especificação JPA usada pelo Spring Boot por padrão.

### 1.2 Configuração do `application.properties`
Para que a aplicação suba corretamente, é necessário configurar a conexão com o banco. Sem isso, o `ConnectionManager` falha no startup.

**Parâmetros Obrigatórios:**
1.  `spring.datasource.url`: A string de conexão JDBC (ex: `jdbc:mysql://localhost:3306/db_events`).
2.  `spring.datasource.username`: Usuário do banco.
3.  `spring.datasource.password`: Senha do banco.
4.  `spring.jpa.properties.hibernate.dialect`: O "sotaque" SQL que o Hibernate deve falar (ex: MySQL, PostgreSQL, Oracle). Isso permite que o framework gere SQL otimizado para o banco específico.

---

## 2. Modelagem de Entidades (ORM)

### 2.1 Anotações Básicas
Para transformar uma classe Java em uma tabela, usamos anotações do pacote `jakarta.persistence`:
* **`@Entity`**: Marca a classe como uma entidade persistível.
* **`@Table(name = "tbl_user")`**: Define o nome exato da tabela no banco, caso seja diferente do nome da classe.
* **`@Id`**: Indica a chave primária.
* **`@GeneratedValue(strategy = ...)`**: Define como o ID é gerado.
    * `GenerationType.IDENTITY`: O banco gera o ID (auto-incremento do MySQL).
    * `GenerationType.AUTO`: O provider escolhe a melhor estratégia.

### 2.2 Boas Práticas: Wrappers vs Primitivos
Ao definir atributos numéricos (como ID ou chaves estrangeiras), deve-se preferir **Wrapper Classes** (`Integer`, `Long`) em vez de primitivos (`int`, `long`).
* **Motivo:** Tipos primitivos iniciam com `0` por padrão. Wrappers iniciam com `null`.
* **Semântica:** Em banco de dados, `0` é um valor, enquanto `null` é a ausência de valor. Usar Wrappers permite saber se o campo foi realmente preenchido ou se está vazio.

### 2.3 Mapeamento de Colunas (`@Column`)
Permite detalhar as restrições do banco direto no Java:
* `name`: Nome da coluna física.
* `length`: Tamanho (padrão 255 para Strings).
* `nullable = false`: Define a coluna como `NOT NULL`.
* `unique = true`: Cria uma constraint de unicidade (ex: e-mails não repetidos).

---

## 3. Modelagem de Relacionamentos Avançados

### 3.1 Relacionamento N:N com Atributos (O Problema)
Imagine a relação entre `User` e `Session` (Sessão de evento).
* Um usuário vai a muitas sessões.
* Uma sessão tem muitos usuários.
* **Cenário Simples:** `@ManyToMany` resolve, criando uma tabela de junção oculta com apenas os dois IDs.
* **Cenário Real:** Precisamos saber a **data da inscrição** ou o **nível do ingresso** (Gold, Silver). O `@ManyToMany` padrão não suporta atributos extras na tabela de junção.

### 3.2 A Solução: Entidade Associativa (`Subscription`)
Para adicionar colunas na relação, quebramos o N:N em dois relacionamentos 1:N:
1.  Criamos uma nova entidade `Subscription` (Inscrição).
2.  `User` tem `@OneToMany` para `Subscription`.
3.  `Session` tem `@OneToMany` para `Subscription`.
4.  `Subscription` contém os atributos extras (`createdAt`, `level`).

### 3.3 Chaves Compostas (`@Embeddable` e `@EmbeddedId`)
A tabela `tbl_subscription` tem uma chave primária composta (User ID + Session ID). No JPA, modelamos isso criando uma classe separada para a chave.

1.  **Classe da Chave (`SubscriptionId`):**
    * Anotada com `@Embeddable`.
    * Contém os objetos `User` e `Session` (que são `@ManyToOne`).
    * Implementa `Serializable`.
2.  **Entidade Principal (`Subscription`):**
    * Usa `@EmbeddedId` para incluir a chave composta.
    * Isso isola a complexidade da chave e mantém o código elegante.

---

## 4. Diferenças de Mapeamento

| Anotação | Uso | Exemplo |
| :--- | :--- | :--- |
| **@OneToMany** | Um para Muitos | Uma Conferência tem várias Sessões. |
| **@ManyToOne** | Muitos para Um | Várias Sessões pertencem a uma Conferência. |
| **@JoinColumn** | Define a chave estrangeira (FK) | `name="tbl_conference_id_conference"`. |
| **@EmbeddedId** | Injeta uma chave composta | Usado na entidade `Subscription`. |
