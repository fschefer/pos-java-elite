package fundamentos.colecoes;

import java.util.Objects;

public class Produto {
    private int id;
    private String nome;
    private double preco;

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }

    // Essencial para Sets e Maps não permitirem duplicados "reais"
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id == produto.id && Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + "'}";
    }
}