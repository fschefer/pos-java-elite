# Aula 02: Estruturas de Dados (Collections)

Professor: Francisco Isidro Massetto


## 1. As Três Grandes Interfaces 📐

### A. List (Listas Ordenadas)

Uma sequência de elementos que permite duplicatas e mantém a ordem de inserção.

- **ArrayList:** Rápida para leitura, mas lenta para remover itens no meio (precisa deslocar o resto)
    
- **Vector:** Versão "segura" para Multithreading (sincronizada), garantindo consistência se várias partes do código mexerem nela ao mesmo tempo.
    
- **LinkedList:** Ideal para muitas inserções e remoções (usa "elos" entre os dados)
    

### B. Set (Conjuntos Matemáticos)

Coleção que **não permite elementos duplicados**.

- **HashSet:** Não garante nenhuma ordem, mas é extremamente rápida
    
- **Regra de Ouro:** Para o `Set` saber se um item é repetido, o objeto DEVE implementar `equals()` e `hashCode()`.
    

### C. Map (Dicionários Chave-Valor)

Trabalha com pares. Cada valor é encontrado através de uma **Chave única**.

- **HashMap:** Se você inserir uma chave que já existe, ele substitui o valor antigo pelo novo.
    
- **Performance:** É a estrutura mais rápida para buscas exaustivas.
    

---

## ⚡ 2. Análise de Performance: Big O Notation

O professor demonstrou que a estrutura escolhida impacta diretamente o tempo de execução do sistema.

| **Estrutura** | **Complexidade de Busca** | **Comportamento**                                                   |
| ------------- | ------------------------- | ------------------------------------------------------------------- |
| **List**      | $O(n)$ (Linear)           | Quanto mais itens, mais tempo demora (precisa percorrer um por um). |
| **Map**       | $O(1)$ (Constante)        | O tempo é o mesmo, não importa se você tem 10 ou 1 milhão de itens. |