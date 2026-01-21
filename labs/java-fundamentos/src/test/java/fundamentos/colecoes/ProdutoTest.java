package fundamentos.colecoes;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    @Test
    void testSetNaoPermiteDuplicatas() {
        // Sets são como caixas mágicas: se você tenta colocar um brinquedo igual, ele ignora
        Set<Produto> conjunto = new HashSet<>();
        Produto p1 = new Produto(1, "Computador", 1500.0);
        Produto p2 = new Produto(1, "Computador", 1500.0); // ID e Nome iguais

        conjunto.add(p1);
        conjunto.add(p2);

        // Mesmo tentando adicionar 2 vezes, o tamanho deve ser 1 porque o equals() barrou
        assertEquals(1, conjunto.size());
    }

    @Test
    void testMapSubstituiValorComMesmaChave() {
        // Maps são dicionários: se você muda o significado da palavra, a antiga some
        Map<Integer, String> mapa = new HashMap<>();
        mapa.put(1, "Computador");
        mapa.put(1, "Mesa"); // Mesma chave (1)

        // O valor "Computador" foi atropelado pela "Mesa"
        assertEquals("Mesa", mapa.get(1));
        assertEquals(1, mapa.size());
    }

    @Test
    void testListPermiteDuplicatas() {
        // Listas são como uma fila: todo mundo que chega entra no fim
        List<Produto> lista = new ArrayList<>();
        Produto p = new Produto(1, "Mouse", 50.0);

        lista.add(p);
        lista.add(p);

        // Na lista, o mesmo objeto pode aparecer várias vezes
        assertEquals(2, lista.size());
    }
}