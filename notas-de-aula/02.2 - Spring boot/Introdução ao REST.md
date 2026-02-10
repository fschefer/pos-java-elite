

### 1.1 Statelessness (Ausência de Estado)

Uma das características definidoras de uma API REST é ser **stateless**.

- **Conceito:** O servidor não armazena nenhum contexto do cliente (como uma sessão de usuário) entre as requisições. Cada requisição HTTP deve conter todas as informações necessárias para que o servidor a compreenda e processe (ex: quem é o usuário, o que ele quer acessar).
    
- **Contraste com Web Clássica:** Em tecnologias anteriores (JSP, JSF, ASP.NET WebForms), o servidor mantinha um objeto `Session` em memória. O cliente enviava apenas um ID de sessão (cookie), e o servidor recuperava o estado. Em REST, isso não existe, o que facilita a escalabilidade horizontal (adicionar mais servidores sem se preocupar em replicar sessões).
    

### 1.2 Anatomia de um Endpoint

Um erro comum é achar que o endpoint é apenas a URL. Na verdade, um endpoint é a combinação de **URL (Recurso) + Verbo HTTP (Ação)**.

Isso permite o uso da mesma URL para ações distintas, mantendo a API limpa e semântica.

Exemplo com o recurso `/produtos`:

- `GET /produtos`: Listar todos.
    
- `POST /produtos`: Criar novo.
    
- `PUT /produtos`: Atualizar.
    
- `DELETE /produtos`: Remover.
    

---

## 2. Semântica dos Verbos HTTP

A escolha do verbo correto comunica a intenção da operação.

|**Verbo**|**Ação Principal**|**Detalhes Técnicos**|
|---|---|---|
|**GET**|**Consulta**|Deve ser seguro e idempotente (não altera dados no servidor). Usado para recuperar listas ou itens únicos.|
|**POST**|**Criação**|Envia dados no corpo (`body`) da requisição. Não é idempotente (chamar 2x cria 2 recursos). Usado também para ações complexas (ex: Login, Processar Venda).|
|**PUT**|**Substituição**|Atualiza um recurso por completo. Se você enviar um JSON apenas com o "nome" para um endpoint PUT, os outros campos (preço, descrição) podem ser apagados ou setados como nulos.|
|**PATCH**|**Atualização Parcial**|Altera apenas os campos enviados, mantendo os dados originais dos campos omitidos. (Mais complexo de implementar corretamente).|
|**DELETE**|**Remoção**|Remove o recurso identificado.|

---

## 3. Implementação Técnica no Spring Boot

### 3.1 Manipulação de Respostas com `ResponseEntity`

Retornar apenas o objeto (ex: `public Produto salvar()`) limita a API, pois o status será sempre 200 OK, mesmo que haja problemas de validação.

Para controle total, utilizamos a classe wrapper `ResponseEntity<T>`. Ela permite configurar o corpo da resposta, os headers e, crucialmente, o **HTTP Status Code**.

**Status Codes Essenciais:**

- `200 OK`: Sucesso genérico (GET, PUT).
    
- `201 Created`: Recurso criado com sucesso (POST).
    
- `400 Bad Request`: Erro do cliente (ex: JSON malformado, parâmetros inválidos).
    
- `404 Not Found`: Recurso não encontrado (ex: ID inexistente no banco).
    

### 3.2 Diferença: `@PathVariable` vs `@RequestParam`

- **`@PathVariable`**: Parte integrante da URL, identifica um recurso único. Obrigatório.
    
    - Ex: `GET /produtos/10` -> O `10` é o ID.
        
    - Uso: `buscarPorId(@PathVariable int id)`.
        
- **`@RequestParam`**: Parâmetros de filtro ou ordenação, geralmente opcionais, passados após o `?`.
    
    - Ex: `GET /produtos/sort?order=asc` -> O `asc` é o parâmetro.
        
    - Uso: `ordenar(@RequestParam(required = false) String order)`.
        

---