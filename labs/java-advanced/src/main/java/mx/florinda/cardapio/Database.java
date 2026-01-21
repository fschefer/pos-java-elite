package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Database {

    // Simulação de banco de dados com diferentes estruturas
    private final Map<Long, ItemCardapio> itensPorId = new HashMap<>();
    private final Map<ItemCardapio, LocalDateTime> historicoVisualizacao = new WeakHashMap<>();
    private final Map<ItemCardapio, BigDecimal> auditoriaPrecos = new IdentityHashMap<>();
    private final Set<CategoriaCardapio> categoriasEmPromocao = EnumSet.noneOf(CategoriaCardapio.class);
    private final Map<CategoriaCardapio, String> descricoesCategorias = new EnumMap<>(CategoriaCardapio.class);

    public Database() {
        // Carga inicial de dados
        adicionarItem(new ItemCardapio(1L, "Refresco de Limão", "Parece tamarindo", CategoriaCardapio.BEBIDAS, new BigDecimal("2.99"), null));
        adicionarItem(new ItemCardapio(2L, "Sanduíche de Presunto", "O clássico", CategoriaCardapio.PRATOS_PRINCIPAIS, new BigDecimal("15.00"), null));
        adicionarItem(new ItemCardapio(3L, "Churros", "Dona Florinda", CategoriaCardapio.SOBREMESAS, new BigDecimal("5.00"), null));

        categoriasEmPromocao.add(CategoriaCardapio.SOBREMESAS);
        descricoesCategorias.put(CategoriaCardapio.BEBIDAS, "Refrescam e confundem");
    }

    private void adicionarItem(ItemCardapio item) {
        itensPorId.put(item.id(), item);
    }

    public List<ItemCardapio> listarTodos() {
        return new ArrayList<>(itensPorId.values());
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

            // IdentityHashMap permite guardar a versão antiga do objeto como chave
            auditoriaPrecos.put(itemAntigo, novoPreco);
            System.out.println("Preço alterado: " + novoPreco);
        });
    }

    public void imprimirRelatorios() {
        System.out.println("Histórico (WeakHashMap) Size: " + historicoVisualizacao.size());
        System.out.println("Auditoria (IdentityHashMap) Size: " + auditoriaPrecos.size());
    }
}