package fundamentos.oo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContaTest {
    @Test
    void contaComumNaoDevePermitirSaldoNegativo() {
        ContaBancaria comum = new ContaBancaria(111, "Flavio");
        comum.creditar(100.0);
        assertFalse(comum.debitar(150.0));
        assertEquals(100.0, comum.getSaldo());
    }

    @Test
    void contaEspecialDevePermitirUsoDoLimite() {
        ContaEspecial especial = new ContaEspecial(222, "Schefer", 200.0);
        especial.creditar(100.0);
        // Polimorfismo: debitar 250 (usando 100 de saldo e 150 de limite)
        assertTrue(especial.debitar(250.0));
        assertEquals(-150.0, especial.getSaldo());
    }
}