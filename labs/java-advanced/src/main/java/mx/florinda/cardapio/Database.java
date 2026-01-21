package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Banco de dados em memória que demonstra o uso de coleções thread-safe e especializadas.
 */
public class Database {

    // 1. ConcurrentSkipListMap: Thread-safe e mantém ordem natural das chaves (IDs).
    private final Map<Long, ItemCardapio> itensPorId = new ConcurrentSkipListMap<>();

    // 2. WeakHashMap: Chaves removidas pelo GC quando não há mais referências fortes.
    private final Map<ItemCardapio, LocalDateTime> historicoVisualizacao = new WeakHashMap<>();

    // 3. IdentityHashMap: Compara chaves por identidade (==) para auditoria.
    private final Map<ItemCardapio, BigDecimal> auditoriaPrecos = new IdentityHashMap<>();

    // 4. Coleções especializadas para Enums.
    private final Set<CategoriaCardapio> categoriasEmPromocao = EnumSet.noneOf(CategoriaCardapio.class);
    private final Map<CategoriaCardapio, String> descricoesCategorias = new EnumMap<>(CategoriaCardapio.class);

    public Database() {
        inicializarDadosDefault();
    }

    private void inicializarDadosDefault() {
        adicionarItem(new ItemCardapio(1L, "Refresco de Limão", "Parece tamarindo", CategoriaCardapio.BEBIDAS, new BigDecimal("2.99"), null));
        adicionarItem(new ItemCardapio(2L, "Sanduíche de Presunto", "O clássico", CategoriaCardapio.PRATOS_PRINCIPAIS, new BigDecimal("15.00"), null));
        adicionarItem(new ItemCardapio(3L, "Churros", "Dona Florinda", CategoriaCardapio.SOBREMESAS, new BigDecimal("5.00"), null));

        categoriasEmPromocao.add(CategoriaCardapio.SOBREMESAS);
        descricoesCategorias.put(CategoriaCardapio.BEBIDAS, "Refrescam e confundem");
    }

    // --- Métodos de Operações de Negócio ---

    public void adicionarItem(ItemCardapio item) {
        itensPorId.put(item.id(), item);
    }

    public List<ItemCardapio> listarTodos() {
        return new ArrayList<>(itensPorId.values());
    }

    /**
     * Retorna o total de itens presentes no banco de dados.
     */
    public int totalItens() {
        return itensPorId.size();
    }

    public Optional<ItemCardapio> buscarPorId(Long id) {
        return Optional.ofNullable(itensPorId.get(id));
    }

    public void registrarVisualizacao(Long id) {
        buscarPorId(id).ifPresent(item -> {
            historicoVisualizacao.put(item, LocalDateTime.now());
            System.out.println("Visualizado: " + item.nome());
        });
    }

    public boolean removerItem(Long id) {
        return itensPorId.remove(id) != null;
    }

    public void alterarPreco(Long id, BigDecimal novoPreco) {
        buscarPorId(id).ifPresent(itemAntigo -> {
            ItemCardapio itemNovo = itemAntigo.alterarPreco(novoPreco);
            itensPorId.put(id, itemNovo);
            auditoriaPrecos.put(itemAntigo, novoPreco);
        });
    }

    // --- Métodos de Relatórios ---

    public void imprimirRelatorios() {
        imprimirHistoricoVisualizacao();
        imprimirAuditoria();
        imprimirItensOrdenadosPorPreco();
    }

    public void imprimirHistoricoVisualizacao() {
        System.out.println("\n--- Histórico (WeakHashMap) ---");
        System.out.println("Tamanho atual: " + historicoVisualizacao.size());
        historicoVisualizacao.forEach((item, data) ->
                System.out.println(item.nome() + " visto em " + data));
    }

    public void imprimirAuditoria() {
        System.out.println("\n--- Auditoria de Preços (IdentityHashMap) ---");
        auditoriaPrecos.forEach((itemAntigo, novoPreco) ->
                System.out.printf("Item: %s | Antigo: %s -> Novo: %s%n",
                        itemAntigo.nome(), itemAntigo.preco(), novoPreco));
    }

    public void imprimirItensOrdenadosPorPreco() {
        System.out.println("\n--- Ordenado por Preço (TreeSet) ---");
        Set<ItemCardapio> ordenados = new TreeSet<>(Comparator.comparing(ItemCardapio::preco));
        ordenados.addAll(itensPorId.values());
        ordenados.forEach(i -> System.out.println(i.nome() + " - R$ " + i.preco()));
    }
}