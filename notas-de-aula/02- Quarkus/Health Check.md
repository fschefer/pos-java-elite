## 1. O Problema de Sistemas Distribuídos

Em uma arquitetura de microsserviços, a comunicação via rede é o maior ponto de falha. Se um serviço A chama o serviço B e o B está lento ou fora do ar, o serviço A pode travar esperando a resposta, gerando um efeito cascata em todo o cluster.

---

## 2. Padrões de Tolerância a Falhas

A especificação **MicroProfile Fault Tolerance** fornece anotações para proteger a aplicação.

###  Timeout

Define um tempo máximo de espera. Se o serviço remoto não responder nesse tempo, a execução é abortada para não prender a thread.

- **Anotação:** `@Timeout(3000)` (3000ms ou 3s).
    

###  Fallback

Define um plano B. Se a operação principal falhar (por Timeout ou Exception), um método alternativo é executado.

- **Anotação:** `@Fallback(fallbackMethod = "nomeDoMetodo")`.
    
- **Regra:** O método de fallback deve ter a **mesma assinatura** (mesmo retorno e mesmos parâmetros) do método original.
    

### ⚡ Circuit Breaker (O Disjuntor)

Evita chamar repetidamente um serviço que sabidamente está com problemas. Funciona como um disjuntor elétrico.

- **Fechado (Closed):** O fluxo passa normalmente.
    
- **Aberto (Open):** Após atingir uma taxa de falhas, o circuito abre e **bloqueia** novas chamadas imediatamente (sem tentar ir à rede), retornando erro ou fallback.
    
- **Meio-Aberto (Half-Open):** Após um tempo (`delay`), ele deixa passar algumas requisições de teste. Se derem certo, o circuito fecha; se falharem, abre novamente.
    

**Parâmetros usados na aula:**

- `requestVolumeThreshold = 2`: Analisa janelas de 2 requisições.
    
- `failureRatio = 0.5`: Se 50% falharem (1 de 2), abre o circuito.
    
- `delay = 3000`: Espera 3 segundos antes de tentar novamente.
    
- `successThreshold = 2`: Precisa de 2 sucessos seguidos para fechar o circuito.
    

---

## 3. Health Checks (Kubernetes Probes)

Permitem que o orquestrador (Kubernetes) saiba o estado da aplicação.

|**Tipo**|**Pergunta**|**Ação em caso de falha**|
|---|---|---|
|**Liveness** (`@Liveness`)|"A aplicação está viva?"|O Kubernetes **reinicia** (mata e sobe de novo) o Pod.|
|**Readiness** (`@Readiness`)|"A aplicação está pronta?"|O Kubernetes **para de enviar tráfego** para o Pod até que ele volte a ficar pronto.|

- **Endpoint Padrão:** `/q/health/live`, `/q/health/ready` ou `/q/health`