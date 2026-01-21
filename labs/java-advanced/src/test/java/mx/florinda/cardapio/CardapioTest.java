package mx.florinda.cardapio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardapioTest {

    // --- TESTES DE DOMÍNIO (Records e Enums) ---

    @Test
    @DisplayName("ItemCardapio: Deve criar e garantir imutabilidade")
    void testeItemCardapio() {
        // Testa o Record e o Enum CategoriaCardapio
        ItemCardapio itemOriginal = new ItemCardapio(
                99L, "Teste", "Desc",
                CategoriaCardapio.BEBIDAS, BigDecimal.TEN, null
        );

        // Valida getters do Record
        assertEquals("Teste", itemOriginal.nome());
        assertEquals(BigDecimal.TEN, itemOriginal.preco());

        // Testa o método auxiliar alterarPreco (Cria novo objeto?)
        ItemCardapio itemAlterado = itemOriginal.alterarPreco(BigDecimal.TWO);

        assertNotEquals(itemOriginal, itemAlterado, "O record deve ser imutável (gerar nova instância)");
        assertEquals(BigDecimal.TWO, itemAlterado.preco(), "O preço deve ter sido alterado na nova instância");
        assertEquals(BigDecimal.TEN, itemOriginal.preco(), "O original deve permanecer intacto");
    }

    // --- TESTES DE BANCO DE DADOS (Collections) ---

    @Test
    @DisplayName("Database: Deve inicializar com os 3 itens padrão")
    void testeDatabaseInicializacao() {
        Database db = new Database();
        List<ItemCardapio> itens = db.listarTodos();

        assertFalse(itens.isEmpty(), "A lista não deveria estar vazia");
        assertEquals(3, itens.size(), "O banco deveria começar com 3 itens (Refresco, Sanduíche, Churros)");
    }

    @Test
    @DisplayName("Database: Deve remover item corretamente (HashMap)")
    void testeRemoverItem() {
        Database db = new Database();
        Long idParaRemover = 2L; // Sanduíche

        boolean removeu = db.removerItem(idParaRemover);
        assertTrue(removeu, "O método deve retornar true ao remover item existente");

        Optional<ItemCardapio> busca = db.buscarPorId(idParaRemover);
        assertTrue(busca.isEmpty(), "O item não deveria mais existir no mapa");
    }

    @Test
    @DisplayName("Database: Deve alterar preço e manter auditoria")
    void testeAlterarPreco() {
        Database db = new Database();
        Long id = 1L; // Refresco (2.99)
        BigDecimal novoPreco = new BigDecimal("5.50");

        db.alterarPreco(id, novoPreco);

        // Verifica se atualizou no mapa principal
        ItemCardapio itemAtualizado = db.buscarPorId(id).orElseThrow();
        assertEquals(novoPreco, itemAtualizado.preco());
    }

    // --- TESTES DE SERIALIZAÇÃO (Simula GeradorJson) ---

    @Test
    @DisplayName("Integração: Gson deve gerar JSON corretamente")
    void testeGeracaoJson() {
        // Simula a lógica da classe GeradorJson
        Database db = new Database();
        List<ItemCardapio> itens = db.listarTodos();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(itens);

        // Validações do JSON gerado
        assertNotNull(json);
        assertTrue(json.contains("\"id\": 1"), "JSON deve conter ID 1");
        assertTrue(json.contains("Refresco de Limão"), "JSON deve conter o nome do item");
        assertTrue(json.contains("BEBIDAS"), "JSON deve conter a categoria (Enum)");

        System.out.println("JSON gerado e validado!");
    }
}