
A segurança é um dos componentes mais críticos no desenvolvimento de APIs modernas. Proteger endpoints e garantir que apenas utilizadores autorizados tenham acesso aos recursos requer uma arquitetura sólida. No ecossistema Spring, essa responsabilidade é delegada ao **Spring Security**, um _framework_ poderoso e altamente customizável.

Estas notas aprofundam a configuração da infraestrutura de segurança, a transição de um modelo de sessão para um modelo sem estado (_stateless_) e a criação de filtros de interceção de requisições.

---

## 1. O Paradigma _Stateless_ em APIs REST

Aplicações web tradicionais operam num modelo com estado (_stateful_), onde o servidor guarda o histórico do utilizador logado através de sessões (usando _cookies_, por exemplo). No entanto, as APIs REST modernas devem ser estritamente **sem estado (_stateless_)**.

Isto significa que a API não guarda a lembrança de que um utilizador fez login há cinco minutos. Para o servidor, cada requisição é única e independente. Consequentemente, para aceder a rotas protegidas, o cliente precisa de apresentar uma prova de identidade em **todas** as requisições, funcionando exatamente como um crachá de acesso corporativo. Na prática, este "crachá" materializa-se no uso de um **Token** (como o JWT) enviado no cabeçalho HTTP.

---

## 2. O Comportamento Padrão do Spring Security

Apenas ao adicionar a dependência `spring-boot-starter-security` no projeto (`pom.xml`), o Spring Boot aplica de imediato um mecanismo padrão de defesa conhecido como _Basic Auth_.

O que acontece por padrão:

- Todos os _endpoints_ da aplicação são instantaneamente bloqueados.
    
- É gerada uma página de login HTML padrão que interceta as requisições.
    
- O _framework_ gera no _console_ (durante o arranque) uma senha alfanumérica aleatória associada ao utilizador padrão `user`.
    

Embora este comportamento seja útil para proteger rapidamente uma aplicação web simples, ele é incompatível com APIs REST, pois introduz telas de login HTML e gestão de sessão que violam o princípio _stateless_ e impossibilitam o acesso automatizado por dispositivos como IoT ou aplicações _frontend_ desacopladas (React/Angular).

---

## 3. A Cadeia de Filtros (`SecurityFilterChain`)

Para desativar o comportamento padrão e implementar a segurança baseada em tokens, é necessário configurar a **Cadeia de Filtros de Segurança**. O Spring Security funciona como uma série de "portões" (filtros) pelos quais a requisição passa antes de chegar ao _Controller_.

Criamos uma classe anotada com `@Configuration` e `@EnableWebSecurity`, e expomos um _Bean_ que retorna um objeto `SecurityFilterChain`. Dentro desta configuração, aplicamos três passos obrigatórios:

### Passo 1: Desativar o CSRF

O _Cross-Site Request Forgery_ (CSRF) é um ataque comum em aplicações baseadas em sessão e _cookies_. Como a nossa API operará exclusivamente através de _tokens_ em cabeçalhos (sem estado), o CSRF perde a sua utilidade de proteção e deve ser desativado (`http.csrf().disable()`) para não bloquear requisições legítimas.

### Passo 2: Roteamento e Permissões

Definimos com precisão cirúrgica o que é público e o que é privado através do `authorizeHttpRequests`.

- **Rotas Públicas:** Utilizamos `requestMatchers("/open").permitAll()` para permitir que qualquer cliente (mesmo sem token) aceda a recursos abertos, como páginas de registo ou recuperação de senha.
    
- **Rotas Privadas:** Adicionamos `anyRequest().authenticated()` no fim da cadeia para garantir que todas as outras rotas (não declaradas explicitamente como públicas) exijam autenticação prévia.
    

### Passo 3: Injeção do Filtro Customizado

Para que o Spring valide os nossos _tokens_ em vez de pedir um formulário de login, ordenamos que o nosso filtro personalizado (que extrai o _token_) seja executado **antes** do filtro padrão do Spring Security (o `UsernamePasswordAuthenticationFilter`), utilizando o método `addFilterBefore`.

---

## 4. O Intercetador: `OncePerRequestFilter`

A classe central que verifica o "crachá" do utilizador herda de `OncePerRequestFilter`. Esta herança garante arquiteturalmente que a lógica de verificação de segurança é executada rigorosamente **uma única vez por requisição**, otimizando a performance.

O fluxo de processamento (método `doFilterInternal`) ocorre da seguinte forma:

1. **Extração do Cabeçalho:** O filtro inspeciona o objeto `HttpServletRequest` à procura do cabeçalho chamado `Authorization`.
    
2. **Validação do Formato:** Verifica se o valor encontrado possui a semântica padrão `Bearer <token>`. A palavra "Bearer" indica que o utilizador é o "portador" daquele bilhete de acesso. Se o cabeçalho não existir ou estiver num formato incorreto, a requisição segue adiante como "não autenticada" e será barrada posteriormente pelas regras do `SecurityFilterChain`.
    
3. **Higienização e Validação:** A string "Bearer " é removida (através de um `.replace`), isolando apenas o valor bruto do _token_. O _token_ é então passado para uma classe utilitária (como um `TokenUtil`) responsável por decodificar e validar a sua autenticidade e validade.
    

---

## 5. O Registo Oficial: `SecurityContextHolder`

Se o passo anterior confirmar que o _token_ é perfeitamente válido, o sistema precisa de comunicar este sucesso ao núcleo do _framework_ Spring Security para que a requisição não seja rejeitada com um erro _403 Forbidden_.

Isto é feito instanciando um objeto do tipo `UsernamePasswordAuthenticationToken`. Este objeto atua como uma representação em memória do utilizador autenticado e as suas permissões (_roles_).

Em seguida, este objeto é anexado ao **Contexto de Segurança Global** da requisição através do comando `SecurityContextHolder.getContext().setAuthentication(auth)`.

A partir deste momento, para o ciclo de vida desta requisição específica, o Spring reconhece formalmente o utilizador como autenticado. Por fim, o filtro chama `filterChain.doFilter(request, response)`, enviando a requisição higienizada e legitimada em direção ao seu destino final, o _Controller_.

---
## 1. Criptografia de Senhas com BCrypt

Guardar senhas em texto limpo (_plain text_) na base de dados é uma falha crítica de segurança. Para resolver isto, o Spring Security fornece o `BCryptPasswordEncoder`, um algoritmo de _hashing_ robusto que embaralha a senha antes de a guardar.

- **A Estrutura do Hash BCrypt:** Quando guardamos uma senha (ex: "12345"), o BCrypt gera uma string complexa no formato `$2a$10$...`.
    
    - O `$2a$` indica a versão do algoritmo.
        
    - O `10` indica o _cost factor_ (fator de custo ou número de iterações/ciclos). Quanto maior este número, mais forte é a criptografia, mas mais processamento é exigido do servidor.
        
- **Implementação:** No momento de criar um utilizador (POST `/users`), instanciamos o `BCryptPasswordEncoder` e fazemos o `encode()` da senha original antes de invocar o `save()` no repositório.
    

## 2. O Fluxo de Login Customizado

Como desativámos a página de login padrão do Spring (para manter a API _stateless_), precisamos de criar um endpoint próprio de autenticação.

1. **O Endpoint `/login`:** Criamos um método `@PostMapping("/login")` que recebe as credenciais (username e password) no corpo da requisição (`@RequestBody User user`). Não se esqueça de permitir o acesso a esta rota no `WebSecurityConfig` (`requestMatchers("/login").permitAll()`).
    
2. **Validação (Matches):** No `UserService`, procuramos o utilizador na base de dados. Se ele existir, usamos a função `encoder.matches(senhaCrua, senhaCriptografada)` para verificar se a senha enviada corresponde ao _hash_ guardado no banco.
    
3. **Retorno do Token:** Se as senhas coincidirem, geramos o JWT e devolvemos ao cliente usando um DTO chamado `MyToken` (um _record_ simples contendo apenas o campo `token`).
    

## 3. Geração e Assinatura do JWT (`TokenUtil.encode`)

A geração do token é feita recorrendo à biblioteca JJWT (`io.jsonwebtoken.Jwts`).

- **Atributos (Claims):** Um JWT carrega informações conhecidas como _claims_ (reivindicações). Configuramos:
    
    - `Subject`: O "assunto" do token, normalmente o `username` do utilizador.
        
    - `Issuer`: A entidade que emitiu o token (ex: "professor_isidro" ou o nome da sua API).
        
    - `Expiration`: O tempo de validade do token (calculado somando a data atual com o tempo de expiração em milissegundos).
        
- **A Chave Secreta (Secret Key):** Para assinar digitalmente o token, precisamos de uma chave privada forte. O algoritmo `HMAC-SHA256` exige uma chave com um mínimo de 256 bits, o que equivale a pelo menos 32 caracteres alfanuméricos contínuos.
    
- **Construção:** Usamos `Jwts.builder().setSubject(...).setIssuer(...).setExpiration(...).signWith(chave).compact()` para gerar a String final do JWT.
    

## 4. Decodificação e Validação do JWT (`TokenUtil.decode`)

O processo inverso ocorre quando a API recebe uma requisição em rotas protegidas.

- Utilizamos `Jwts.parser().verifyWith(chave).build().parseSignedClaims(token)` para quebrar o token recebido e extrair o seu _Payload_ (_Claims_).
    
- A própria biblioteca verifica se a assinatura matemática é válida.
    
- Extraímos `getSubject()`, `getIssuer()` e `getExpiration()`.
    
- Se tudo estiver válido e o token não estiver expirado, criamos e retornamos o `UsernamePasswordAuthenticationToken`. Esse objeto será injetado no `SecurityContextHolder` pelo nosso Filtro.
    

## 5. Tratamento Global de Exceções de Autenticação

Se um utilizador tentar fazer login com uma conta inexistente ou senha incorreta, lançamos uma `RuntimeException`. Para não espalhar `try-catch` pelos _Controllers_, criamos um `ControllerAdvice` (ex: `ControllerExceptionHandler`) com um método anotado com `@ExceptionHandler(RuntimeException.class)`. Este interceptador captura a falha de login e devolve uma resposta HTTP limpa com o status `403 Forbidden` (Acesso Negado) e a mensagem de erro.

## 6. Introdução ao OAuth2 (Google / Social Login)

Embora a gestão local de utilizadores e JWTs seja excelente, o mercado atual tende a delegar a responsabilidade de autenticação para grandes provedores (Google, GitHub, Microsoft) usando o padrão **OAuth2**.

- **O Fluxo Conceptual:** 1. A sua aplicação _Frontend_ (React/Angular) exibe o botão "Login with Google". 2. O utilizador autentica-se nos servidores da Google. 3. A Google envia um JWT diretamente para o seu _Frontend_. 4. O _Frontend_ extrai os dados (Email, Nome, Foto) ou envia o JWT original para a sua API Spring Boot. 5. A sua API atua como validadora: confia no facto de que, se a Google diz que o utilizador é válido (e a assinatura bate certo via `JWTDecoder`), a sua API permite o acesso ou cria o registo do utilizador sem **nunca guardar ou transacionar a senha na sua base de dados**.