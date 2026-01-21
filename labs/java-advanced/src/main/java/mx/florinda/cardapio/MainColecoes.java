package mx.florinda.cardapio;

import java.math.BigDecimal;

public class MainColecoes {
    public static void main(String[] args) throws InterruptedException {
        Database db = new Database();

        System.out.println("--- 1. Uso normal ---");
        db.registrarVisualizacao(1L);
        db.registrarVisualizacao(2L);

        System.out.println("--- 2. Auditoria de Preço ---");
        db.alterarPreco(1L, new BigDecimal("3.50"));

        System.out.println("--- 3. Teste de Memória (WeakHashMap) ---");
        db.removerItem(2L);
        System.out.println("Solicitando Garbage Collector...");
        System.gc();
        Thread.sleep(1000); // Pausa para o GC atuar

        System.out.println("Verificação final:");
        db.imprimirRelatorios();
        // O item removido deve sumir do histórico (WeakHashMap)
    }
}