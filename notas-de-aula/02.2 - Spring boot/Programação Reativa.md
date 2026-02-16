## 1. O Paradigma Reativo e o Problema da Espera Síncrona

Na arquitetura web tradicional (imperativa/bloqueante), cada requisição HTTP recebida pelo servidor ocupa uma _thread_ (linha de execução) até que o processo seja totalmente concluído. O grande problema ocorre quando a sua API precisa de consumir uma **API Externa Lenta** (como emissão de notas fiscais na Receita Federal, processamento de ficheiros pesados, etc.).

- **O gargalo:** Se a API externa demora 5 segundos a responder, a _thread_ da sua aplicação fica "congelada" e ociosa durante todo esse tempo. Se tiver milhares de requisições simultâneas, o servidor esgota rapidamente as suas _threads_ e a sua API cai (timeout).
    
- **A Solução Reativa:** Com o Spring WebFlux, a aplicação liberta a _thread_ imediatamente para atender outros clientes. A sua API regista uma "promessa" de execução em _background_, devolve uma resposta provisória ao utilizador (como um número de protocolo) e processa os dados de forma assíncrona assim que a API externa os envia.
    

## 2. Conceitos Fundamentais: Mono e Flux

No ecossistema reativo do Spring (baseado no _Project Reactor_), lidamos com fluxos de dados não bloqueantes em vez de objetos diretos. A resposta de uma operação assíncrona é sempre encapsulada em dois tipos de classes (_Wrappers_):

- **`Mono<T>`:** Representa uma promessa que vai devolver **0 ou 1 elemento** no futuro. É o equivalente reativo de um objeto singular (ex: encontrar um `User` por ID).
    
- **`Flux<T>`:** Representa uma promessa que vai devolver **0 a N elementos** no futuro. É o equivalente reativo de uma `List` ou `Collection`.
    

## 3. O Protagonista: `WebClient`

Para fazer chamadas a outras APIs de forma não bloqueante, abandonamos o antigo `RestTemplate` e passamos a utilizar o **`WebClient`**. Para facilitar a sua injeção em várias classes de serviço (`@Service`), a melhor prática é declará-lo como um `@Bean` dentro de uma classe anotada com `@Configuration`.

## 4. Arquitetura do Cenário de Exemplo (Emissão Assíncrona)

Para demonstrar o poder reativo, foi criado um projeto simulando um despachante de documentos fiscais:

1. **O Banco de Dados:** Configurado o H2 numa tabela `tbl_doc_fiscal` que guarda o ID, o protocolo gerado e o documento final (JSON salvo como String).
    
2. **A Chamada (WebClient):** É feita uma requisição GET construindo a URI da _Slow API_ externa. A extração dos dados é feita através de `retrieve().bodyToMono(String.class)`.
    
3. **O Desfecho (_Callbacks_):** Usamos o método `.doOnNext(...)` para dizer ao sistema o que fazer quando a resposta chegar com sucesso (gravar o documento no banco associado ao protocolo gerado). O método `.doOnError(...)` trata as falhas.
    
4. **O Gatilho (`.subscribe()`):** Na programação reativa, nada acontece até que haja uma "inscrição" no fluxo. O método `.subscribe()` é o que dispara efetivamente a requisição para segundo plano (_background_).
    

## 5. Endpoints do Controlador Reativo

- **`POST /solicitar`:** Recebe a intenção de processamento, gera imediatamente um número de protocolo (UUID), dispara o serviço externo em _background_ e **responde na mesma fração de segundo** um HTTP `202 Accepted` ao cliente, junto com o número do protocolo gerado.
    
- **`GET /consultar/{protocolo}`:** O cliente volta algum tempo depois a este endpoint com o seu protocolo. A API consulta a base de dados (H2) e retorna o documento processado, caso a API lenta externa já tenha finalizado e o método `doOnNext` já o tenha salvo.
    

---