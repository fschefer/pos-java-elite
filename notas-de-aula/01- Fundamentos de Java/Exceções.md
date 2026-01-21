# Tratando exceções 

1. Introdução: Construindo Aplicações Resilientes
Um sistema robusto não é aquele que nunca falha, mas aquele que sabe lidar com o inesperado sem "capotar" na frente do usuário. Em Java, o tratamento de exceções é o mecanismo oficial para desviar o fluxo normal de execução quando algo dá errado e tentar uma recuperação.

---

## 🚦 2. A Hierarquia do Caos (Throwable)
Tudo que pode ser "lançado" (thrown) em Java herda da classe `java.lang.Throwable`. Ela se divide em dois grandes reinos

### A. Erro (`java.lang.Error`) 💥
* **Definição:** Problemas graves na infraestrutura da JVM. Geralmente, não há o que fazer a não ser deixar o programa encerrar.
* **Exemplos:** `OutOfMemoryError` (Faltou RAM), `StackOverflowError` (Loop infinito), `VirtualMachineError`.
* **Ação do QA:** Identificar o gargalo de infraestrutura. Não se usa `try-catch` aqui.

### B. Exceção (`java.lang.Exception`) ⚠️
* **Definição:** Condições anormais que a aplicação pode prever e tratar.
* **Exemplos:** Arquivo não encontrado, falha de conexão, erro de cálculo.
* **Ação do QA:** Validar se o sistema captura o erro e exibe uma mensagem amigável.

---

## ⚖️ 3. Tipos de Exceções (A Grande Divisão)

| Tipo                            | Herança            | Comportamento                                                                                                        | Quando usar?                                                          |
| :------------------------------ | :----------------- | :------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------- |
| **Unchecked** (Não Verificadas) | `RuntimeException` | O compilador **não obriga** o tratamento. Geralmente indica falha de lógica do programador (bug) ou erro de entrada. | Validações de negócio (ex: preço negativo), nulos, índices inválidos. |
| **Checked** (Verificadas)       | `Exception`        | O compilador **obriga** o tratamento (`try-catch` ou `throws`). Indica falha externa previsível.                     | Conexões com banco, leitura de arquivos, APIs externas.               |

---

##  4. Mecanismos de Tratamento

### Blocos Clássicos (`try-catch-finally`)
* **`try`**: "Tente executar este bloco perigoso."
* **`catch`**: "Se der este erro específico, faça isso." (Do mais específico para o genérico) 
* **`finally`**: "Dando certo ou errado, execute isso." Usado para fechar recursos, mas hoje substituído pelo *try-with-resources*.

### Multi-Catch (Java 7+)
Permite tratar exceções diferentes com a mesma lógica, evitando repetição de código.
> Sintaxe: `catch (ErroA | ErroB e) { ... }`.

### Try-With-Resources (Java 7+)
A forma moderna e segura de lidar com recursos (arquivos, scanners, conexões). Se a classe implementa `AutoCloseable`, o Java fecha o recurso automaticamente ao final do bloco `try`, dispensando o `finally`.

---

##  5. Exceções Personalizadas (Domain Exceptions)
Para um código limpo, evite lançar exceções genéricas (`Exception`). Crie exceções que falem a língua do negócio.

* **Dica de Ouro:** Prefira estender `RuntimeException` para não poluir o código de quem usa sua classe com `try-catch` obrigatórios desnecessários, a menos que seja uma falha de infraestrutura crítica.

---

## ✅ Checklist de QA para Exceções 
1.  **Não engula exceções:** Um `catch` vazio é um crime silencioso. No mínimo, logue o erro.
2.  **Seja específico:** Capture `FileNotFoundException` antes de `IOException`.
3.  **Mensagens Úteis:** "Erro 123" não ajuda ninguém. Use "Falha ao processar pagamento: saldo insuficiente".