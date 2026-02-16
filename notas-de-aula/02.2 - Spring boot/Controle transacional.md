
A gestão transacional é um dos pilares mais críticos no desenvolvimento de sistemas empresariais, financeiros e de alta complexidade. Compreender o seu funcionamento é essencial para garantir a integridade dos dados quando a aplicação precisa de executar múltiplas operações que dependem umas das outras.

## 1. O Problema da Falta de Transação

Em bases de dados relacionais, muitas lógicas de negócio exigem a execução de um bloco de operações (vários `INSERTs`, `UPDATEs` ou `DELETEs` encadeados).

O exemplo clássico é uma **transferência bancária**:

1. Retira-se dinheiro da Conta A (Débito).
    
2. Coloca-se o dinheiro na Conta B (Crédito).
    
3. Regista-se a operação no histórico.
    

**O Risco:** Se o sistema ou a rede falhar exatamente a meio deste processo (por exemplo, após debitar a Conta A, mas antes de creditar a Conta B), o dinheiro simplesmente "desaparece" do sistema, gerando uma inconsistência grave e dados órfãos. Para evitar que operações interdependentes fiquem num estado pela metade, utilizamos as **Transações**.

## 2. Propriedades ACID (O Coração das Transações)

Para que a gestão transacional seja verdadeiramente eficaz e segura, o sistema de base de dados e o _framework_ devem garantir quatro propriedades fundamentais, conhecidas pelo acrónimo **ACID**:

- **A - Atomicidade (A Regra do "Tudo ou Nada"):** Uma transação é uma unidade indivisível. Ou todas as operações dentro do bloco são executadas com sucesso (gerando um _Commit_), ou nenhuma delas é efetivada. Se ocorrer qualquer erro durante o percurso, o sistema realiza um _Rollback_ automático, desfazendo todas as alterações e devolvendo a base de dados ao seu estado original.
    
- **C - Consistência:** A base de dados transita sempre de um estado válido para outro estado válido. Ao final da transação, todas as regras de negócio, restrições (_constraints_), chaves estrangeiras e gatilhos devem ser respeitados (por exemplo, garantir que o saldo de uma conta não fique negativo se essa for uma regra imposta).
    
- **I - Isolamento:** Num ambiente de alta concorrência, dezenas de utilizadores podem estar a fazer transferências em simultâneo. O isolamento garante que uma transação em curso não interfira noutra, e que os dados intermédios (ainda não comitados) de uma transação fiquem invisíveis para as restantes.
    
- **D - Durabilidade:** Assim que o _Commit_ é emitido com sucesso, as alterações são persistidas de forma permanente na base de dados física (gravadas no disco rígido). Desta forma, mesmo que ocorra uma falha de energia ou uma queda do servidor no milissegundo seguinte, os dados estarão a salvo e não serão perdidos.
    

## 3. A Magia do Spring Boot: A Anotação `@Transactional`

Antigamente (ou utilizando JDBC puro), os programadores precisavam de abrir conexões, iniciar a transação, fazer blocos `try-catch` complexos e invocar manualmente `connection.commit()` ou `connection.rollback()`.

No ecossistema Spring, essa complexidade é abstraída de forma elegante utilizando a anotação `@Transactional`, geralmente aplicada nos métodos da camada de **Service**.

- **A Arquitetura de _Proxy_:** Por debaixo dos panos, o Spring utiliza AOP (Programação Orientada a Aspetos) para criar um _Proxy_ (um invólucro) em torno do método anotado.
    
- **O Fluxo de Sucesso (_Commit_):** O _Proxy_ abre a transação antes de a primeira linha de código do método ser executada. Se o método chegar ao fim sem lançar nenhuma exceção, o _Proxy_ efetiva o _Commit_ na base de dados.
    
- **O Fluxo de Falha (_Rollback_):** Se o seu código (ou o próprio sistema) lançar uma `RuntimeException` em qualquer ponto do método (por exemplo, um `InvalidBalanceException`), o _Proxy_ interceta essa exceção, interrompe a execução e comanda o _Rollback_ à base de dados, revertendo todas as operações SQL anteriores daquele método.
    

## 4. Exemplo Prático de Integração

Numa arquitetura multicamadas simulando uma transferência bancária (utilizando uma base de dados H2 configurada para gravar em ficheiro, de modo a testar a Durabilidade real), o fluxo com `@Transactional` é orquestrado assim:

1. **Recuperação de Dados:** O sistema busca as contas de origem e destino nos repositórios.
    
2. **Crédito:** Atualiza o saldo da conta de destino (soma o valor) e faz o `save` preliminar.
    
3. **Débito:** Atualiza o saldo da conta de origem (subtrai o valor). Se nesta fase a conta ficar com saldo negativo, a entidade lança uma exceção de validação. Como o método está anotado com `@Transactional`, o Spring interceta o erro e desfaz o crédito que foi efetuado no passo 2.
    
4. **Registo:** Se não houver erro de saldo, cria o objeto `Transaction` (o recibo da operação) e guarda no repositório.
    
5. **Commit:** O método termina com sucesso e o Spring garante a durabilidade final.