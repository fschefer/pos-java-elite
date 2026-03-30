## 1. Requisitos de Negócio vs. Características Arquiteturais

Para pensar como um arquiteto, a primeira etapa é separar o que é funcional do que é estrutural/qualitativo.

- **Requisitos Funcionais (Negócio):** São as funcionalidades específicas que o software deve fornecer para atender ao domínio do problema. Ex: Cadastro de usuário, processamento de pagamento, tela de login.
    
- **Características Arquiteturais (Qualidades):** São atributos de qualidade (as famosas "-ilities" ou requisitos não-funcionais) que definem _como_ e _quão bem_ o sistema realiza suas funções. Ex: Desempenho, escalabilidade, segurança. O termo "não-funcional" muitas vezes soa como algo secundário, mas essas características são vitais para o sucesso do projeto.
    

---

## 2. Critérios de uma Característica Arquitetural

Como saber se um requisito é, de fato, uma característica arquitetural? Ele deve atender a três critérios principais:

1. **Não é derivado diretamente do domínio do negócio:** O usuário comum raramente pedirá "um cluster Kubernetes com 5ms de latência", mas sim "que o sistema seja rápido".
    
2. **Influencia significativamente a estrutura (Design):** Exige decisões que afetam a organização dos componentes. Ex: A exigência de alta segurança demandará um módulo isolado de criptografia.
    
3. **É crítico para o sucesso da aplicação:** Se essa característica falhar (ex: latência alta em uma bolsa de valores), o propósito do sistema como um todo falha.
    

---

## 3. Explícitas vs. Implícitas

O papel do arquiteto exige proatividade para mapear necessidades que muitas vezes não estão nos documentos de requisitos.

- **Explícitas:** Estão documentadas em contratos ou manuais. Ex: "Suportar até 5.000 usuários simultâneos" ou "Estar em conformidade com a LGPD".
    
- **Implícitas:** Não estão escritas, mas o negócio pressupõe que existirão. Ex: Proteção contra invasões (Segurança) e estar no ar o tempo todo (Disponibilidade). Se o arquiteto não as prever, será cobrado por elas em produção.
    

---

## 4. Categorias de Características Arquiteturais

### A. Operacionais (Foco em Execução)

- **Desempenho (Performance):** Capacidade e tempo de resposta (latência e throughput).
    
- **Disponibilidade (Availability):** Percentual de tempo no ar (uptime), impactando topologias de cluster e failover.
    
- **Escalabilidade (Scalability):** Capacidade de crescer (scale up/out) para picos de uso e diminuir (scale down) para poupar custos.
    
- **Confiabilidade/Robustez (Reliability/Robustness):** Tolerância a falhas. Em caso de erros, o sistema deve ter degradação graciosa (_graceful degradation_) sem cair totalmente.
    
- **Continuidade do Negócio:** Planejamento para desastres (Disaster Recovery) e backups em falhas catastróficas.
    

### B. Estruturais (Foco no Código e Design)

- **Modularidade e Manutenibilidade:** Facilidade de aplicar mudanças no código sem quebrar outras partes.
    
- **Extensibilidade:** Facilidade de acoplar novas funções (ex: plugar um novo gateway de pagamento).
    
- **Reutilização:** Capacidade de criar componentes que servem para múltiplos projetos.
    
- **Portabilidade:** Rodar em múltiplos ambientes (AWS, GCP, on-premise) ou servidores de aplicação.
    
- **Suportabilidade e Configurabilidade:** Facilidade de alterar o comportamento via configurações (sem novo deploy) e monitorar via logs e tracing.
    

### C. Transversais / Cross-Cutting (Impacto Global)

- **Segurança:** Autenticação, autorização, criptografia e proteção contra intrusos.
    
- **Privacidade e Compliance:** Adequação a leis como LGPD, HIPAA ou padrões como PCI (proteção de cartões de crédito), envolvendo anonimização de dados.
    
- **Acessibilidade e Usabilidade:** Adaptação da interface para pessoas com deficiência e facilidade geral de uso.
    
- **Retenção e Arquivamento:** Políticas de tempo de vida e descarte de logs e dados de negócio.
    

---

## 5. Trade-offs e a Armadilha da Complexidade

- **O Efeito "Cobertor Curto":** As características competem entre si. Se você aumenta demais a segurança (adicionando criptografia pesada), você inevitavelmente prejudica a performance (maior tempo de processamento).
    
- **Complexidade Acumulada (A Síndrome do Navio Vasa):** Em 1628, o rei da Suécia exigiu que o navio de guerra _Vasa_ tivesse todas as melhores características simultaneamente (velocidade, poder de fogo dobrado, transporte de tropas). O resultado? O navio ficou tão pesado e instável que afundou na viagem inaugural ao primeiro sopro de vento. Tentar suportar "todas" as características de arquitetura de uma só vez condena o projeto ao fracasso por excesso de complexidade.
    
- **Pragmatismo:** "Não busque a melhor arquitetura, mas sim a menos pior" (_Never shoot for the best architecture, but rather the least worst architecture_). Escolha no máximo 3 a 5 características-chave prioritárias para o sucesso do negócio e crie um balanço realista em torno delas.
    

---

## 6. Como Identificar e Priorizar

1. **Extrair do Domínio:** Entenda o negócio. Se o _time-to-market_ é essencial, priorize agilidade e manutenibilidade.
    
2. **Analisar Requisitos Explícitos:** São os frutos mais baixos da árvore ("low hanging fruits"). Leia os contratos e especificações.
    
3. **Conhecer Padrões da Indústria:** Estude as leis vigentes e as exigências do nicho de mercado (ex: sistemas financeiros exigem o padrão PCI).
    
4. **Envolver os Stakeholders:** Converse com os interessados e faça-os ranquear as prioridades para criar consenso e alinhar expectativas.