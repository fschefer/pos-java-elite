
## 1. Arquitetura em Camadas (Layered Architecture / N-Tier)

É o estilo arquitetural mais tradicional e amplamente utilizado na indústria, considerado o "padrão de fato" para muitas aplicações corporativas corporativas.

### Visão Geral

- O sistema é dividido em camadas lógicas horizontais (ex: Apresentação, Lógica de Negócio, Persistência, Banco de Dados).
    
- Geralmente é implementado como um **monólito** (uma única unidade de _deploy_).
    
- Frequentemente surge por acidente ("Architecture by Implication" ou "Accidental Architecture") devido à **Lei de Conway**: equipes separadas por especialidade técnica (devs de Front-end, devs de Back-end, DBAs) produzem sistemas divididos exatamente nessas mesmas fronteiras técnicas.
    

### Fluxo de Comunicação

- **Camadas Fechadas (Closed Layers):** O modelo ideal e mais seguro. Uma camada só pode se comunicar estritamente com a camada imediatamente abaixo dela. Isso garante o isolamento: mudanças no banco de dados não afetam o front-end, pois a camada de negócio absorve o impacto.
    
- **Camadas Abertas (Open Layers / Fast-Lane Reader):** Uma camada pode "pular" a intermediária e acessar diretamente a inferior (ex: Apresentação acessando diretamente o Banco de Dados). Ocorre quando tenta-se resolver o _anti-pattern_ do **Architecture Sinkhole** (buraco negro arquitetural), onde as camadas intermediárias não agregam lógica e servem apenas de "passa-fita". Regra: só abra camadas com forte justificativa e documente! Se o _Sinkhole_ representar cerca de 20% das requisições, é aceitável manter as camadas fechadas. Se for a grande maioria, talvez seja a arquitetura errada.
    

### Vantagens

- **Simplicidade:** Fácil de entender, familiaridade da indústria (bom para _onboarding_ de novos desenvolvedores).
    
- **Custo e Agilidade Inicial:** Excelente para começar projetos novos, MVPs ou aplicações pequenas, pois o desenvolvimento inicial é muito rápido.
    
- **Separação de Preocupações (Separation of Concerns):** Facilita testes isolados em componentes de uma mesma camada (ex: testar a lógica sem subir a UI) e o desenvolvimento por especialistas.
    

### Desvantagens e Limitações

Por ser um monólito orientado a dados, herda vários problemas quando escala:

- **Acoplamento Oculto:** O modelo é fortemente amarrado aos dados (geralmente um banco relacional único). Mudar um campo da tabela exige atualizações em cascata pela camada de Persistência, Negócios e Apresentação (alto acoplamento global).
    
- **Escalabilidade (Elasticity):** Baixa. Não é possível escalar partes específicas. Para suportar carga na interface, é preciso escalar a aplicação inteira verticalmente ou replicar horizontalmente tudo (desperdício de recursos).
    
- **Tolerância a Falhas e Deploy:** Baixa. Um erro na aplicação derruba todo o sistema. O ciclo de entrega (deploy) é mais lento e arriscado (é "tudo ou nada").
    

---

## 2. Arquitetura Pipeline (Pipes and Filters)

Organiza o processamento em um fluxo unidirecional composto de etapas independentes (Filtros) conectadas por canais (Pipes). É fortemente inspirada nos comandos Unix (ex: `cat arquivo | grep erro | wc -l`), streams reativas e MapReduce. Tradicionalmente implementada dentro de um monólito.

### Componentes

- **Pipes (Canais):** Canais de comunicação que ligam os filtros. Direcionam a informação (payloads, objetos, strings) do filtro anterior estritamente para o próximo filtro, de forma unidirecional ponto-a-ponto.
    
- **Filters (Filtros):** Unidades de processamento pequenas, _stateless_ (sem estado), independentes e focadas em uma única tarefa. Tarefas complexas são resolvidas encadeando filtros simples, não criando filtros monstros.
    

### Tipos de Filtros

1. **Producer (Origem/Pump):** O ponto de partida. Não recebe entrada, apenas gera ou coleta os dados e envia para o primeiro Pipe. Ex: Leitor de arquivo, Webhook.
    
2. **Transformer (Transformador):** Recebe o dado, aplica uma operação (conversão, cálculo, criptografia, enriquecimento) e envia o dado modificado adiante. É o coração lógico do pipeline.
    
3. **Tester (Validador):** Avalia os dados contra critérios. Pode descartar o dado ou direcioná-lo para caminhos diferentes do fluxo.
    
4. **Consumer (Destino/Sink):** O ponto final. Recebe o dado e executa a persistência ou a saída final (ex: salva no banco, envia para fila de mensagens).
    

### Vantagens e Limitações

- **Prós:** Altíssima reutilização, testabilidade unitária excelente (filtros isolados), modularidade superior e separação cristalina de tarefas. Ideal para transformações pesadas de dados, ETLs (Extract, Transform, Load).
    
- **Contras:** Desempenho geral restrito ao filtro mais lento (o fluxo empaca se um passo for demorado). Assim como as camadas, no modelo tradicional monolítico sofre das mesmas dores: escalabilidade da aplicação por completo, tolerância a falhas global (um erro para o pipeline) e acoplamento de deploy.
    
- **Pipelines e Microsserviços:** O padrão monolítico pode ser evoluído para uma arquitetura orientada a eventos distribuída, onde cada "filtro" se torna um microsserviço independente consumindo eventos, ganhando escalabilidade, mas perdendo a baixa latência e a simplicidade de orquestração do monólito.