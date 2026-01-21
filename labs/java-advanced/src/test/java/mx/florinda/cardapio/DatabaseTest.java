package mx.florinda.cardapio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    @DisplayName("Funcionalidade Básica: Adicionar e Contar")
    void testAdicionarItem() {
        Database db = new Database();
        int totalInicial = db.totalItens(); // Deve ser 3

        ItemCardapio novoItem = new ItemCardapio(
                99L, "Teste Unitário", "Desc",
                CategoriaCardapio.BEBIDAS, BigDecimal.TEN, null
        );

        db.adicionarItem(novoItem);

        assertEquals(totalInicial + 1, db.totalItens());
        assertTrue(db.buscarPorId(99L).isPresent());
    }

    @Test
    @DisplayName("🔥 Teste de Concorrência (Stress Test)")
    void testConcorrenciaExtrema() throws InterruptedException {
        Database db = new Database();
        int totalInicial = db.totalItens(); // 3

        // Configuração do Teste de Carga
        int numeroDeThreads = 100;
        int insercoesPorThread = 100;
        int totalEsperado = totalInicial + (numeroDeThreads * insercoesPorThread);

        // ExecutorService: Simula os usuários simultâneos
        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);

        // CountDownLatch: Garante que o teste só termina quando todas as threads acabarem
        CountDownLatch latch = new CountDownLatch(numeroDeThreads);

        // Gerador de IDs únicos para evitar sobrescrita (AtomicInteger é thread-safe)
        AtomicInteger idGenerator = new AtomicInteger(1000);

        System.out.println("Iniciando ataque com " + numeroDeThreads + " threads...");

        for (int i = 0; i < numeroDeThreads; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < insercoesPorThread; j++) {
                        long id = idGenerator.getAndIncrement();
                        ItemCardapio item = new ItemCardapio(
                                id, "Item " + id, "Stress Test",
                                CategoriaCardapio.ENTRADAS, BigDecimal.ONE, null
                        );
                        db.adicionarItem(item);
                    }
                } finally {
                    latch.countDown(); // Avisa que essa thread terminou
                }
            });
        }

        // Bloqueia o teste até que o contador chegue a zero (todas as threads terminaram)
        latch.await();
        executor.shutdown();

        System.out.println("Total no Banco: " + db.totalItens());
        System.out.println("Total Esperado: " + totalEsperado);

        // ASSERT FINAL: Se o Database não for thread-safe, este número será menor (perda de dados)
        assertEquals(totalEsperado, db.totalItens(),
                "O banco de dados perdeu itens durante a concorrência! Verifique se está usando ConcurrentMap.");
    }
}