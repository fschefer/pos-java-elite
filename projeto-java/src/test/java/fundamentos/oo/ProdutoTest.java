package fundamentos.oo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {
    @Test
    void naoDevePermitirPrecoNegativo() {
        Produto p = new Produto(1, "Notebook", 3500.0, 5);
        assertThrows(IllegalArgumentException.class, () -> p.setPreco(-10.0));
    }

    @Test
    void deveAlterarDescricaoComSuccesso() {
        Produto p = new Produto(1, "Velho", 10.0, 1);
        p.setDescricao("Novo");
        assertEquals("Novo", p.getDescricao());
    }
}