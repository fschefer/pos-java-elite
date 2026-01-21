package fundamentos.oo;

public class Produto {
    private int codigo;
    private String descricao;
    private double preco;
    private int estoque;

    public Produto(int codigo, String descricao, double preco, int estoque) {
        this.codigo = codigo;
        this.descricao = descricao;
        setPreco(preco); // Validação via setter
        this.estoque = estoque;
    }

    // Getters e Setters Completos
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) {
        if (preco < 0) throw new IllegalArgumentException("Preço inválido!");
        this.preco = preco;
    }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }
}