package fundamentos.oo;

public class ContaEspecial extends ContaBancaria {
    private double limite;

    public ContaEspecial(int numero, String titular, double limite) {
        super(numero, titular);
        this.limite = limite;
    }

    @Override
    public boolean debitar(double valor) {
        // Regra polimórfica: permite saldo negativo até o limite
        if (this.saldo + this.limite >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }
}