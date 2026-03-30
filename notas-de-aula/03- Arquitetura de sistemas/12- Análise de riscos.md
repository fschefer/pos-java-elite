
## 1. O Contexto das Escolhas Arquiteturais

Não existe um estilo arquitetural universalmente perfeito ou "o melhor de todos". A escolha ideal sempre dependerá do contexto.

- **Fatores de Influência:** A escolha é guiada por experiências passadas (evitando repetir erros) e pela constante evolução do ecossistema tecnológico (novos frameworks e ferramentas como Kubernetes).
    
- **Alinhamento ao Negócio:** A arquitetura precisa se adaptar a fusões, expansões, mudanças de leis (compliance) e custos do negócio.
    

### Critérios de Escolha

Para decidir qual caminho seguir, o arquiteto deve avaliar:

1. **Domínio do Negócio:** Entender as necessidades e peculiaridades específicas da indústria.
    
2. **Requisitos de Qualidade (-ilities):** Definir o que é mais importante (ex: escalabilidade, performance, disponibilidade).
    
3. **Arquitetura de Dados:** Decidir entre banco único (monolítico) ou distribuído, avaliando necessidades de transações ACID ou consistência eventual.
    
4. **Fatores Organizacionais:** Avaliar o orçamento, prazo e a maturidade da equipe para lidar com tecnologias complexas.
    

---

## 2. Decisões Arquiteturais e Anti-Patterns

As decisões tomadas pelo arquiteto guiam o time técnico e garantem o alinhamento com os objetivos da empresa. O fluxo correto exige coletar informações, avaliar _trade-offs_, justificar, documentar e comunicar a todos os envolvidos.

### Anti-Patterns na Tomada de Decisão (O que evitar)

- **Cover Your Assets (Proteja-se):** Ocorre quando o arquiteto fica paralisado pelo medo de errar e ser responsabilizado. O projeto trava. A solução é assumir a responsabilidade e decidir com as informações disponíveis.
    
- **Groundhog Day (Feitiço do Tempo / Dia da Marmota):** A mesma decisão é discutida repetidas vezes porque ninguém se lembra o motivo da escolha original. Solução: documentar os motivos.
    
- **Email-Driven Architecture:** Decisões cruciais ficam perdidas em e-mails ou chats (Slack/Discord) e parte da equipe não fica sabendo. Solução: ter uma "Fonte Única da Verdade" (Single Source of Truth) centralizada.
    

---

## 3. ADRs (Architecture Decision Records)

Para resolver os problemas de documentação, utilizamos os ADRs. São documentos curtos de texto que capturam uma decisão arquitetural importante.

- **Estrutura de um ADR:**
    
    - **Título e Status:** (ex: Proposto, Aceito, Descontinuado).
        
    - **Contexto:** Qual problema está sendo resolvido.
        
    - **Decisão:** A escolha técnica efetuada.
        
    - **Consequências:** Os impactos e _trade-offs_ assumidos.
        
- **Benefícios:** Garantem rastreabilidade, facilitam o _onboarding_ de novos membros e podem até servir de base para validações automatizadas de código (_fitness functions_). Documentos devem ser versionados para manter o histórico das mudanças ao longo do tempo.
    

---

## 4. Análise e Gestão de Riscos

Todo sistema possui falhas em potencial, sejam operacionais ou falhas de segurança. O papel do arquiteto é antecipá-las.

![risk assessment matrix impact probability, gerada com IA](https://encrypted-tbn1.gstatic.com/licensed-image?q=tbn:ANd9GcSSuQYQEvdQArF6BZg0da0nLy2gigpJYDOrvKBmo3kNfJ5MzdLmX8lMjndkhMexPLMT_8Vz18bBjHmNukVN0kmrfWd5Npi68ZhQ7Ryt2ogPO5_1HxE)

Shutterstock

Explorar

- **A Matriz de Risco:** Avalia a severidade de um problema através da fórmula: `Risco = Impacto × Probabilidade`.
    
- Isso permite atribuir um valor numérico ao risco para categorizá-lo visualmente (verde para baixo risco, amarelo para médio, vermelho para crítico) e priorizar as ações de correção.
    
- **Monitoramento Contínuo:** Envolve produzir relatórios (_Risk Assessment_), filtrar o que é crítico, observar as tendências (se o risco está aumentando ou diminuindo com o tempo) e executar Planos de Mitigação contínuos.
    

---

## 5. Risk Storming

O _Risk Storming_ é uma dinâmica colaborativa criada para mapear as vulnerabilidades de uma arquitetura. É executada em 3 etapas:

1. **Identificação Individual:** Cada arquiteto e líder técnico analisa o diagrama da arquitetura e anota, em formato de Post-its (separados por cores), os potenciais problemas.
    
2. **Discussão em Grupo:** Todos colocam suas anotações no diagrama central. O time debate as áreas de maior preocupação para alcançar um consenso sobre a verdadeira gravidade de cada risco.
    
3. **Brainstorm de Mitigação:** A equipe foca nos riscos críticos e define um plano de ação (ex: adicionar um banco de replicação caso o banco central seja um ponto único de falha).