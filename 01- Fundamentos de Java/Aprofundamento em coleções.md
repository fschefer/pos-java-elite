
O Java oferece implementações específicas para diferentes cenários de performance e ordenação. A escolha correta impacta diretamente a velocidade e o consumo de memória da aplicação.

### 1. Listas (`List`)

- **`ArrayList`**: Baseada em **Array**.
    
    - _Vantagem:_ Acesso muito rápido por índice (`get(i)`).
        
    - _Desvantagem:_ Remoção lenta (precisa deslocar/copiar todos os elementos à direita do removido).
        
- **`LinkedList`**: Baseada em **Nós Encadeados** (Doubly Linked List).
    
    - _Vantagem:_ Inserção e remoção rápidas (basta ajustar os ponteiros `next` e `previous`).
        
    - _Desvantagem:_ Acesso lento (precisa percorrer a lista nó por nó) e maior consumo de memória.
        

### 2. Conjuntos (`Set`) - Sem duplicatas

- **`HashSet`**: Usa `HashMap` internamente.
    
    - _Ordem:_ Não garante ordem.
        
    - _Performance:_ O mais rápido para verificar existência (`contains`).
        
- **`LinkedHashSet`**: Usa `LinkedHashMap`.
    
    - _Ordem:_ Mantém a **ordem de inserção**.
        
- **`TreeSet`**: Usa `TreeMap` (Árvore Rubro-Negra / Red-Black Tree).
    
    - _Ordem:_ Mantém os elementos **ordenados** (Ordem natural ou `Comparator`).
        
    - _Performance:_ Mais lento na inserção (`O(log n)`), mas útil para dados classificados.
        
- **`EnumSet`**: Especializado para `Enum`.
    
    - _Performance:_ Extremamente eficiente. Usa **vetores de bits** (bitwise) internamente. Ocupa pouquíssima memória.
        

### 3. Mapas (`Map`) - Chave/Valor

- **`HashMap`**: Padrão. Usa `hashCode()` e `equals()` para localizar chaves.
    
- **`LinkedHashMap`**: Mantém a ordem de inserção das chaves.
    
- **`TreeMap`**: Mantém as chaves ordenadas.
    
- **`EnumMap`**: Otimizado para chaves que são `Enum`.
    
- **`WeakHashMap`**: **Gestão de Memória**. As chaves são mantidas por referências fracas (_Weak References_). Se a chave não for usada em nenhum outro lugar do sistema, o Garbage Collector (GC) pode removê-la do mapa automaticamente.
    
    - _Uso:_ Caches, Históricos de visualização temporários.
        
- **`IdentityHashMap`**: **Identidade vs Igualdade**.
    
    - Ao contrário dos outros mapas, ele não usa `equals()`. Ele usa `==`.
        
    - Permite chaves duplicadas em valor, desde que sejam instâncias diferentes na memória.
        
    - _Uso:_ Auditoria de alterações onde o objeto muda de estado mas você quer manter o histórico de cada versão do objeto.
        

---


Abaixo, o código consolidado com todas as estruturas discutidas na aula.

### 1. Domínio (`record` e `enum`)

Java

```
package mx.florinda.cardapio;

import java.math.BigDecimal;

// Enum para categorias
public enum CategoriaCardapio {
    ENTRADAS,
    PRATOS_PRINCIPAIS,
    BEBIDAS,
    SOBREMESAS
}

// Record (Imutável, já possui equals/hashCode baseados nos valores)
public record ItemCardapio(
    Long id,
    String nome,
    String descricao,
    CategoriaCardapio categoria,
    BigDecimal preco,
    BigDecimal precoPromocional // Pode ser null
) {
    // Método auxiliar para criar cópia com preço novo (já que record é imutável)
    public ItemCardapio alterarPreco(BigDecimal novoPreco) {
        return new ItemCardapio(id, nome, descricao, categoria, novoPreco, precoPromocional);
    }
}
```

### 2. O Banco de Dados em Memória (`Database.java`)

Esta classe demonstra o uso de **seis** tipos diferentes de coleções.


```
package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Database {

    // 1. HashMap: Busca rápida por ID
    private final Map<Long, ItemCardapio> itensPorId = new HashMap<>();

    // 2. WeakHashMap: Se o item for removido do sistema, sai do histórico automaticamente pelo GC
    private final Map<ItemCardapio, LocalDateTime> historicoVisualizacao = new WeakHashMap<>();

    // 3. IdentityHashMap: Permite chaves "iguais" (valor) mas objetos diferentes (memória)
    // Útil para auditoria onde o Record tem os mesmos dados mas são instâncias diferentes
    private final Map<ItemCardapio, BigDecimal> auditoriaPrecos = new IdentityHashMap<>();

    // 4. EnumSet: Alta performance para categorias em promoção
    private final Set<CategoriaCardapio> categoriasEmPromocao = EnumSet.noneOf(CategoriaCardapio.class);

    // 5. EnumMap: Associar descrições às categorias
    private final Map<CategoriaCardapio, String> descricoesCategorias = new EnumMap<>(CategoriaCardapio.class);

    public Database() {
        // Inicializa dados
        adicionarItem(new ItemCardapio(1L, "Refresco de Limão", "Parece tamarindo", CategoriaCardapio.BEBIDAS, new BigDecimal("2.99"), null));
        adicionarItem(new ItemCardapio(2L, "Sanduíche de Presunto", "O clássico", CategoriaCardapio.PRATOS_PRINCIPAIS, new BigDecimal("15.00"), null));
        adicionarItem(new ItemCardapio(3L, "Churros", "Dona Florinda", CategoriaCardapio.SOBREMESAS, new BigDecimal("5.00"), null));
        
        // Configura EnumSet e EnumMap
        categoriasEmPromocao.add(CategoriaCardapio.SOBREMESAS);
        descricoesCategorias.put(CategoriaCardapio.BEBIDAS, "Refrescam e confundem");
    }

    private void adicionarItem(ItemCardapio item) {
        itensPorId.put(item.id(), item);
    }

    // Retorna List (ArrayList implícito no values())
    public List<ItemCardapio> listarTodos() {
        return new ArrayList<>(itensPorId.values());
    }

    // Busca com Optional
    public Optional<ItemCardapio> buscarPorId(Long id) {
        return Optional.ofNullable(itensPorId.get(id));
    }

    // Registro de visualização usando WeakHashMap
    public void registrarVisualizacao(Long id) {
        buscarPorId(id).ifPresent(item -> {
            historicoVisualizacao.put(item, LocalDateTime.now());
            System.out.println("Visualizado: " + item.nome());
        });
    }

    // Remoção
    public boolean removerItem(Long id) {
        ItemCardapio removido = itensPorId.remove(id);
        return removido != null;
    }

    // Lógica de alteração de preço com Auditoria (IdentityHashMap)
    public void alterarPreco(Long id, BigDecimal novoPreco) {
        buscarPorId(id).ifPresent(itemAntigo -> {
            // Cria nova instância (Record é imutável)
            ItemCardapio itemNovo = itemAntigo.alterarPreco(novoPreco);
            
            // Atualiza o banco principal
            itensPorId.put(id, itemNovo);

            // Registra auditoria: O itemAntigo é a chave.
            // Como é IdentityHashMap, ele guarda o objeto antigo mesmo que o equals() seja igual ao novo em alguns casos.
            auditoriaPrecos.put(itemAntigo, novoPreco);
            
            System.out.println("Preço alterado de " + itemAntigo.preco() + " para " + novoPreco);
        });
    }

    // Métodos para exibir relatórios
    public void imprimirHistoricoVisualizacao() {
        System.out.println("\n--- Histórico (WeakHashMap) ---");
        System.out.println("Tamanho do histórico: " + historicoVisualizacao.size());
        historicoVisualizacao.forEach((k, v) -> System.out.println(k.nome() + " visto em " + v));
    }

    public void imprimirAuditoria() {
        System.out.println("\n--- Auditoria de Preços (IdentityHashMap) ---");
        auditoriaPrecos.forEach((itemAntigo, novoPreco) -> 
            System.out.printf("Item: %s | Antigo: %s -> Novo: %s%n", itemAntigo.nome(), itemAntigo.preco(), novoPreco)
        );
    }
    
    // TreeSet: Ordenação Natural ou Comparator
    public void imprimirItensOrdenadosPorPreco() {
        System.out.println("\n--- Ordenado por Preço (TreeSet) ---");
        // Comparator customizado
        Set<ItemCardapio> ordenados = new TreeSet<>(Comparator.comparing(ItemCardapio::preco));
        ordenados.addAll(itensPorId.values());
        ordenados.forEach(i -> System.out.println(i.nome() + " - " + i.preco()));
    }
}
```

### 3. Testando o Garbage Collector (`Main.java`)

Este código simula o comportamento do `WeakHashMap` ao remover um item e forçar o GC.


```
package mx.florinda.cardapio;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Database db = new Database();

        // 1. Simula uso e visualização
        db.registrarVisualizacao(1L); // Refresco
        db.registrarVisualizacao(2L); // Sanduíche

        db.imprimirHistoricoVisualizacao(); // Deve ter 2 itens

        // 2. Altera preço (Testando IdentityHashMap)
        System.out.println("\nAlterando preços...");
        db.alterarPreco(1L, new BigDecimal("3.50")); // Muda referência no mapa principal
        db.alterarPreco(1L, new BigDecimal("4.00")); // Muda de novo

        db.imprimirAuditoria(); // Deve mostrar o histórico das mudanças

        // 3. Remove item do sistema (Testando WeakHashMap)
        System.out.println("\nRemovendo Sanduíche (ID 2)...");
        db.removerItem(2L); 
        // Nota: O objeto 'Sanduíche' agora só existe como chave no WeakHashMap (referência fraca)

        // 4. Força o Garbage Collector
        System.out.println("Solicitando Garbage Collector...");
        System.gc();
        Thread.sleep(1000); // Dá tempo pro GC trabalhar

        // 5. Verifica se o Sanduíche sumiu do histórico
        db.imprimirHistoricoVisualizacao(); 
        // Se o GC rodou, o tamanho deve ser 1 (só o Refresco), pois a chave do Sanduíche foi coletada.
    }
}
```

### 📝 Resumo da Estratégia de Escolha

|**Necessidade**|**Coleção Recomendada**|
|---|---|
|Lista genérica rápida|`ArrayList`|
|Lista com muita inserção/remoção no meio|`LinkedList`|
|Itens únicos sem ordem|`HashSet`|
|Itens únicos ordenados|`TreeSet`|
|Mapa padrão|`HashMap`|
|Mapa que respeita ordem de inserção|`LinkedHashMap`|
|Cache que limpa memória sozinho|`WeakHashMap`|
|Histórico de alterações de objetos|`IdentityHashMap`|
|Chaves são Enums|`EnumMap` / `EnumSet`|