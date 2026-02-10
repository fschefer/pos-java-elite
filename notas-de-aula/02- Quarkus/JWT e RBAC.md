
### JWT (JSON Web Token)

É um padrão aberto (RFC 7519) para troca de informações seguras. É **Stateless**, ou seja, o servidor não precisa guardar sessão; toda a informação necessária para validar o acesso está no próprio token.

Estrutura do Token:

1. **Header:** Metadados (tipo do token e algoritmo de criptografia).
    
2. **Payload (Claims):** Dados do usuário e da sessão (ex: `sub` (subject/ID), `iss` (issuer), `exp` (expiration), `groups`/`roles`).
    
3. **Signature:** Garante a integridade dos dados. Gerada com uma chave secreta ou chave privada.
    

### RBAC (Role-Based Access Control)

Controle de acesso baseado em papéis. Em vez de dar permissão para o usuário "João", você dá permissão para o perfil "Admin" ou "Subscriber". O usuário herda as permissões das roles que possui no token.

Para acessar os dados do token dentro da aplicação, injetamos o objeto `JsonWebToken`.

> **Importante:** Como o token é específico por requisição, a classe que injeta o `JsonWebToken` deve ser anotada com `@RequestScoped` (ou `@ApplicationScoped` em versões mais recentes que usam proxy, mas `@RequestScoped` é o padrão seguro para contexto de usuário).