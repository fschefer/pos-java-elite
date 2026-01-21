package mx.florinda.cardapio;

import java.math.BigDecimal;

public record ItemCardapio(
        Long id,
        String nome,
        String descricao,
        CategoriaCardapio categoria,
        BigDecimal preco,
        BigDecimal precoPromocional // Pode ser null
) {
    // Método auxiliar para criar cópia com preço novo (record é imutável)
    public ItemCardapio alterarPreco(BigDecimal novoPreco) {
        return new ItemCardapio(id, nome, descricao, categoria, novoPreco, precoPromocional);
    }
}