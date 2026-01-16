package fundamentos.exceptions;

public class Produto {
private String nome;
private double preco;

    public Produto(String nome, double preco) {
        // Validações no construtor ou setters
        this.setNome(nome);
        this.setPreco(preco);
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            // Lança nossa exceção personalizada
            throw new ProdutoInvalidoException("Erro: O nome do produto é obrigatório!");
        }
        this.nome = nome;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            // Usa uma exceção padrão do Java para argumentos ilegais
            throw new IllegalArgumentException("Erro: O preço não pode ser negativo!");
        }
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.format("Produto: %s | R$ %.2f", nome, preco);
    }
}