package fundamentos.streams;

public class Veiculo {
    private String marca;
    private String cor;
    private double preco;
    private int ano;
    private int velocidadeMaxima;

    public Veiculo(String marca, String cor, double preco, int ano, int velocidadeMaxima) {
        this.marca = marca;
        this.cor = cor;
        this.preco = preco;
        this.ano = ano;
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public String getMarca() { return marca; }
    public String getCor() { return cor; }
    public double getPreco() { return preco; }
    public int getAno() { return ano; }
    public int getVelocidadeMaxima() { return velocidadeMaxima; }

    @Override
    public String toString() {
        return String.format("Veiculo [Marca=%s, Cor=%s, Preço=R$ %.2f, Ano=%d, VelMax=%d km/h]",
                marca, cor, preco, ano, velocidadeMaxima);
    }
}