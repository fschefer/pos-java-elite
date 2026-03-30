
## 1. O que é um Componente?

Para pensar em arquitetura de forma prática, é preciso entender a diferença entre a organização lógica e a organização física do código.

- **Módulo:** É um agrupamento estritamente **lógico** de código relacionado (ex: um conjunto de funções ou classes que resolvem um problema).
    
- **Componente:** É a manifestação **física** desse módulo. É o pacote de código compilado e pronto para ser implantado no ambiente de produção. Ex: Um arquivo `.jar` (Java), uma `.dll` (.NET), um _gem_ (Ruby) ou um executável nativo.
    
- **Relação:** Um componente pode conter um ou dezenas de módulos de negócio em seu interior. A relação não é 1 para 1.
    

---

## 2. Escopo dos Componentes

Os componentes podem variar amplamente em tamanho e responsabilidade:

1. **Biblioteca (Library):** O escopo mais simples. Código reutilizável que roda dentro do mesmo processo da aplicação principal. Traz dependências em tempo de compilação.
    
2. **Subsistema/Camada:** Um bloco que agrupa vários módulos para representar uma camada lógica inteira de um monólito (ex: a camada de persistência). A comunicação entre os componentes ocorre através de chamadas de métodos internos.
    
3. **Serviço / Microsserviço:** O escopo mais autônomo. O componente roda em um processo próprio (ou em um container isolado) e se comunica via rede (HTTP, RPC, Mensageria) com o resto do sistema. Tem escala e ciclo de vida totalmente independentes.
    

---

## 3. Papéis na Definição de Componentes

- **Arquiteto de Software:** Define o particionamento macro e a distribuição das responsabilidades. É quem diz _quais_ componentes existirão para satisfazer as características arquiteturais exigidas, sem detalhar o código interno (a menos que impacte o design estrutural).
    
- **Desenvolvedor:** Refina os limites traçados pelo arquiteto através da implementação em código. É responsável por escrever os métodos e organizar as classes internamente, trazendo _feedback_ sobre a viabilidade da arquitetura proposta.
    

---

## 4. Particionamento Arquitetural

Como dividir a arquitetura da sua aplicação? Existem duas formas clássicas:

### A. Particionamento Técnico (Em Camadas)

Os componentes são organizados pelo seu papel técnico na infraestrutura (Ex: Componente de UI, Componente de Business, Componente de Data Access). Os fluxos de negócio cruzam verticalmente todas as camadas.

- **Vantagens:** Separação clara das preocupações técnicas (baixo acoplamento de código), facilidade em reutilizar lógicas comuns e implementar funcionalidades transversais (ex: segurança, logs).
    
- **Desvantagens:** Acoplamento global muito alto em relação aos dados. Mudar uma regra de negócio pequena geralmente exige alterações na Interface, no Serviço e no Banco de Dados. Frequentemente incentiva o uso de um banco de dados monolítico único.
    

### B. Particionamento por Domínio (Domain Partitioning)

Os componentes são agrupados de acordo com os contextos do negócio (inspirado no DDD - Domain-Driven Design). Ex: Componente de Pagamento, Componente de Catálogo.

- **Vantagens:** O código reflete fielmente as regras do mundo real (alta coesão funcional). Facilita enormemente a evolução de um "monólito modular" para uma arquitetura de microsserviços distribuída no futuro.
    
- **Desvantagens:** Alto risco de duplicação de lógica, já que múltiplos componentes de domínio podem precisar resolver problemas técnicos semelhantes e acabam reescrevendo o mesmo código em silos.
    

---

## 5. Como Identificar e Projetar os Componentes?

A descoberta de componentes não é uma ciência exata, mas um processo iterativo. O fluxo sugerido é:

1. **Identificação Inicial:** Propor um conjunto preliminar (e hipotético) baseado no estilo de partição escolhido para cobrir o escopo do negócio.
    
2. **Alocação de Requisitos:** Distribuir os casos de uso e regras de negócio para ver se os blocos fazem sentido. Novas separações surgirão aqui.
    
3. **Análise de Papéis:** Verificar a coesão. O componente está fazendo coisas demais ou de menos? (Busca pela granularidade ideal).
    
4. **Filtro de Características Arquiteturais:** Avaliar se os blocos desenhados atendem aos requisitos não-funcionais. Se um único bloco exige extrema segurança, talvez ele deva ser isolado fisicamente.
    

### A Grande Armadilha: O "Entity Trap"

**Nunca baseie a arquitetura dos seus componentes em tabelas de banco de dados (Diagramas ER)**. Ter um componente estrito de `Cliente` só porque há uma tabela `Cliente` é um erro. Os fluxos do mundo real raramente seguem operações puras de CRUD em entidades únicas. Projetar dessa forma gera arquiteturas engessadas e ineficazes ("Anemic Domain").

---

## 6. Técnicas de Descoberta de Componentes

Em vez de olhar para o banco de dados, use abordagens orientadas a eventos ou comportamento:

- **Actor/Actions:** Mapear quem usa o sistema e as ações realizadas (útil para metodologias tradicionais).
    
- **Event Storming:** Ideal para DDD e Microsserviços. Faz-se um _brainstorming_ em grupo para mapear todos os eventos do negócio em uma linha do tempo e as causas desses eventos.
    
- **Workflow:** Orientado ao fluxo de trabalho e processos sequenciais (passo a passo), bastante útil quando não se usa a mentalidade orientada a eventos.
    

---

## 7. Decisão de Implantação: Monólito vs. Microsserviço

As decisões arquiteturais geralmente levam ao famoso dilema: manter todos os componentes juntos (Monólito) ou separá-los na rede (Distribuído).

### Arquitetura Monolítica

- Implantado como uma única unidade coesa, geralmente conectada a um único banco de dados.
    
- **Vantagens:** Simplicidade de implantação, chamadas internas ultra-rápidas (sem latência de rede) e extrema facilidade para gerenciar transações e orquestrações.
    
- A escolha perfeita se todos os seus componentes compartilham das mesmas exigências técnicas (ex: todos podem escalar juntos de forma razoável).
    

### Arquitetura Distribuída (Microsserviços)

- Componentes rodando em processos distintos, comunicando-se pela rede de computadores. Importante: Microsserviços não significam "código pequeno", mas sim "distribuído e autônomo" (vide Netflix com microsserviços gigantescos de alta coesão e isolamento).
    
- **Desafios:** Introduz pesada complexidade de rede, latência, dificuldades brutais para tratar transações distribuídas e custos operacionais elevados.
    
- A escolha necessária quando os componentes internos possuem exigências técnicas inconciliáveis. Ex: Se o módulo de Cadastro requer estabilidade, mas o de Busca no Catálogo exige escalar loucamente durante picos (ex: Black Friday), a arquitetura distribuída permitirá escalar apenas o serviço de Busca de forma independente.