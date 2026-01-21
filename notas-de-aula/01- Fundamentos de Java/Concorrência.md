
## 1. O Problema da Thread Única

Todo programa Java roda, no mínimo, na **Main Thread**. Em um servidor Socket (`ServerSocket`), se usarmos apenas a thread principal para atender clientes:
1.  O servidor aceita uma conexão (`accept()`).
2.  Processa a requisição (lê arquivos, faz cálculos).
3.  Só então volta a ouvir novos clientes.
4.  **Consequência:** Clientes formam fila. Se o processamento demorar, ocorre **Timeout**.

## 2. Soluções de Concorrência

### A. Thread por Requisição (Raw Threads)
Criar uma nova thread para cada cliente que chega.
```java
new Thread(() -> trataRequisicao(socket)).start();
````

- **Vantagem:** Atende todos simultaneamente. Alta vazão.
    
- **Problema:** Criação de threads é custosa (CPU/Memória). Sem limite, pode causar **OutOfMemoryError** e derrubar o servidor.
    

### B. Thread Pool (`ExecutorService`) 

A solução profissional. Criamos um "banco" de threads reutilizáveis com tamanho fixo.

Java

```
ExecutorService pool = Executors.newFixedThreadPool(50);
pool.execute(() -> trataRequisicao(socket));
```

- **Vantagem:** Limita o uso de recursos (CPU/RAM).
    
- **Resiliência:** Se chegarem 1000 requisições, 50 são processadas e 950 aguardam na fila, protegendo o servidor de sobrecarga.
    

---

## 3. O Perigo do Estado Compartilhado 

Quando múltiplas threads tentam ler e escrever na mesma variável ou coleção ao mesmo tempo, ocorre **corrupção de dados**.

- **Exemplo:** Dois clientes dão `POST` para adicionar um item. Ambos leem que o tamanho da lista é 10, ambos escrevem na posição 11. Resultado: Um sobrescreve o outro. O total fica 11, mas deveria ser 12.
    

### Coleções Thread-Safe (`java.util.concurrent`)

As coleções padrão (`ArrayList`, `HashMap`) **não são** seguras para threads. Devemos usar as alternativas concorrentes:

|**Coleção Padrão (Não Safe)**|**Alternativa Thread-Safe**|**Característica**|
|---|---|---|
|`HashMap`|**`ConcurrentHashMap`**|O mais usado. Alta performance, bloqueia apenas segmentos do mapa.|
|`TreeMap`|**`ConcurrentSkipListMap`**|Mantém chaves ordenadas e é thread-safe.|
|`ArrayList`|`CopyOnWriteArrayList`|Bom para muitas leituras e poucas escritas.|
|`HashSet`|`ConcurrentHashMap.newKeySet()`|Conjunto thread-safe.|

---