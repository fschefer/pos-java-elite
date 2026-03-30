
## 1. Introdução à Modularização

- **Módulo:** É uma unidade independente e lógica que faz parte de um sistema maior, agrupando códigos relacionados a um propósito comum (ex: um conjunto de funções ou classes).
    
- **Modularidade:** É o princípio de organizar partes relacionadas em módulos coesos com interfaces bem definidas, o que facilita enormemente a compreensão, manutenção e evolução do software.
    
- **Evolução Histórica:**
    
    - **Pré-OO:** Ocorreu a transição do código monolítico "espaguete" (criticado por Dijkstra no artigo "Go To Considered Harmful") para a programação estruturada e linguagens modulares como _Modula_ e _Ada_.
        
    - **Java:** A modularização evoluiu desde o Java 1.0 (com _namespaces/pacotes_ associados ao sistema de arquivos), passou pelo Java 1.2 (com o agrupamento em arquivos _JAR_) e atingiu a maturidade com o _Module System_ introduzido no Java 9.
        

---

## 2. Coesão

A coesão mede o quanto os componentes internos de um módulo estão relacionados entre si e focados em um único propósito. A regra de ouro é buscar sempre a **alta coesão**, indicando que o módulo "faz uma coisa e faz bem feito".

### Tipos de Coesão (Do melhor para o pior)

- **Funcional (A mais forte):** Todos os elementos contribuem diretamente para uma única função específica.
    
- **Sequencial:** A saída de uma parte serve como entrada para outra parte do mesmo módulo.
    
- **Comunicacional:** As partes operam sobre os mesmos dados, mesmo executando tarefas diferentes.
    
- **Procedural:** As partes precisam ser executadas em uma ordem de tempo específica para o algoritmo funcionar.
    
- **Temporal:** Elementos agrupados por ocorrerem no mesmo intervalo de tempo, como em uma rotina de inicialização do sistema.
    
- **Lógica:** Agrupamento por similaridade ou categoria (ex: classe `StringUtils`), mas sem interdependência funcional.
    
- **Coincidente (ou Acidental):** A pior forma, onde os elementos são agrupados juntos de forma aleatória ou por coincidência, sem relação lógica real.
    

### Métrica de Coesão (LCOM)

- **LCOM (Lack of Cohesion of Methods):** Métrica que contabiliza grupos de métodos em uma classe que _não_ compartilham variáveis ou atributos em comum.
    
- **Interpretação:** Quanto _maior_ o valor do LCOM, _menor_ é a coesão (indicando que a classe provavelmente deveria ser dividida).
    

---

## 3. Acoplamento

O acoplamento mede o grau de dependência entre diferentes módulos. O objetivo arquitetural é sempre manter o **baixo acoplamento**, reduzindo o "efeito cascata" das mudanças.

- **Acoplamento Aferente (Ca):** Número de dependências de _entrada_ (quantos componentes externos dependem deste módulo).
    
- **Acoplamento Eferente (Ce):** Número de dependências de _saída_ (quantos componentes externos este módulo utiliza).
    

### Métricas de Robert Martin (Uncle Bob)

- **Abstractness (A) / Grau de Abstração:** A razão entre elementos abstratos (interfaces/classes abstratas) e concretos. Varia de `0` (tudo concreto) a `1` (tudo abstrato).
    
- **Instability (I) / Instabilidade:** Calculado pela fórmula `Ce / (Ce + Ca)`. Varia de `0` (completamente estável, só dependem dele) a `1` (completamente instável, depende de muitos).
    
- **The Main Sequence (Sequência Principal):** É a linha de equilíbrio ideal definida pela fórmula `A + I = 1`. Distanciar-se dessa linha leva a dois extremos:
    
    - **Zone of Pain (Zona da Dor):** Módulos altamente concretos (baixo A) e instáveis/acoplados (baixo I). Difíceis de manter e quebram facilmente.
        
    - **Zone of Uselessness (Zona de Inutilidade):** Módulos com alta abstração (alto A), mas completamente isolados (alto I). Geralmente código morto ou super-engenharia desnecessária.
        

---

## 4. Connascence

Termo cunhado por _Meilir Page-Jones_, a _connascence_ é uma visão mais qualitativa e detalhada do acoplamento. Diz-se que dois componentes são _connascentes_ quando uma alteração em um exige, obrigatoriamente, uma mudança no outro para manter o funcionamento.

### Connascence Estática (Visível no Código Fonte)

- **De Nome (Name):** Componentes concordam no nome de métodos ou variáveis. Se o nome mudar, todas as chamadas precisam mudar.
    
- **De Tipo (Type):** Módulos dependem de um tipo de dados compartilhado. Ex: um parâmetro mudar de `List<String>` para `List<Object>`.
    
- **De Significado/Convenção (Meaning):** Componentes usam valores literais com significados intrínsecos ("Magic Numbers", ex: 0 = Sucesso).
    

### Connascence Dinâmica (Tempo de Execução)

- **De Execução (Execution):** A ordem de chamada dos componentes importa. O passo B só pode ocorrer após o passo A.
    
- **De Temporização (Timing):** O momento de execução impacta o sistema. Ex: problemas de concorrência e _race conditions_ entre _threads_.
    
- **De Valores (Values):** Componentes dependem de valores correlacionados que precisam ser alterados juntos (ex: altura e largura de um objeto retangular mantido por instâncias separadas).
    
- **De Identidade (Identity):** Partes do sistema dependem da mesmíssima entidade em memória ou chave de banco de dados.
    

### Propriedades da Connascence

- **Força:** Quão rígida é a dependência. As estáticas são menos prejudiciais do que as dinâmicas, pois podem ser facilmente rastreadas e alteradas.
    
- **Localidade:** A dependência é menos danosa se os módulos estiverem muito próximos fisicamente/logicamente no projeto (ex: dentro da mesma classe vs em microsserviços diferentes).
    
- **Grau:** A quantidade de módulos amarrados a uma mesma convenção. Quando o grau é elevado, o impacto de qualquer alteração cresce drasticamente.
    

### Regras para Redução de Connascence

1. **Minimize a global:** Dividindo e encapsulando responsabilidades.
    
2. **Iniba a travessia de fronteiras:** Componentes altamente _conascentes_ devem viver juntos no mesmo módulo.
    
3. **Maximize a local:** Limite as dependências fortes estritamente ao nível intramódulo.