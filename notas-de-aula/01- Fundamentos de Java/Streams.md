
---

## 1. O que são Streams?
A **Streams API**, introduzida no Java 8, é uma ferramenta poderosa para processar sequências de elementos (como Listas) de forma **declarativa** (o que fazer) em vez de **imperativa** (como fazer).
* **Foco:** Ordenação, filtragem, transformação e agregação de dados.
* **Vantagem:** Código mais conciso, elegante e performático, com suporte a processamento paralelo.

---

##  2. Operações Fundamentais

### A. Iteração (`forEach`)
Substitui o `for` tradicional por uma abordagem funcional usando Lambdas.
* **Exemplo:** `lista.stream().forEach(v -> System.out.println(v))`.

### B. Ordenação (`sorted`)
Permite ordenar a lista com base em qualquer atributo, sem precisar implementar `Comparable` na classe original se não quiser.
* **Comparator:** Podemos usar referências de métodos como `Comparator.comparing(Veiculo::getPreco)`.
* **Reverso:** Basta adicionar `.reversed()` ao final do comparador.

### C. Filtragem (`filter`)
Seleciona elementos que atendem a um critério específico (Predicado).
* **Exemplo:** Filtrar apenas carros da marca "Corolla".

### D. Transformação (`map`)
Converte cada elemento do stream em outra coisa.
* **Map to Double:** Extrai apenas o preço de cada veículo para cálculos matemáticos.
* **Map de Objeto:** Cria novos objetos baseados nos originais (ex: converter atributos para maiúsculo).

### E. Redução e Agregação
Operações que "esmagam" o stream em um único resultado.
* **Funções Prontas:** `average()` (média), `max()`, `min()` e `sum()`.
* **Tratamento de Vazio:** Como o retorno é um `Optional`, usamos `.orElse(0)` para evitar erros em listas vazias.

---

## 3. Imutabilidade: `toList()` vs `Collectors`
O professor destacou uma diferença crítica na hora de transformar o Stream de volta para uma Lista:

| Método | Tipo de Lista Retornada | Comportamento |
| :--- | :--- | :--- |
| `.toList()` | **Imutável** | Não permite adicionar ou remover itens depois. Se tentar, dá erro. |
| `.collect(Collectors.toList())` | **Mutável** | Cria uma lista comum onde você pode adicionar itens (ex: `add()`) posteriormente. |

---

##  4. O Poder dos Pipelines
A grande força dos Streams é o encadeamento de operações. Em uma única instrução, você pode:
1.  **Filtrar** (só Corollas)
2.  **Mapear** (pegar os preços)
3.  **Calcular** (a média)

Isso elimina a necessidade de criar várias listas temporárias e loops aninhados (`for` dentro de `for`).

---

## ✅ Conclusão do QA
Para testes de massa de dados, Streams são essenciais para validar regras complexas (ex: "verificar se nenhum produto tem preço negativo") usando métodos como `noneMatch` ou `allMatch`. Abandone as listas tradicionais para manipulações complexas.