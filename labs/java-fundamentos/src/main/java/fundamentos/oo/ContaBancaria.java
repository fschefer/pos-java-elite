package fundamentos.oo;

public class ContaBancaria {
    protected double saldo; // Protected para acesso das subclasses
    private int numero;
    private String titular;

    public ContaBancaria(int numero, String titular) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = 0.0;
    }

    public void creditar(double valor) { this.saldo += valor; }

    public boolean debitar(double valor) {
        if (this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    // Getters e Setters
    public double getSaldo() { return saldo; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
}