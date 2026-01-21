package fundamentos.exceptions;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaLoja {
    public static void main(String[] args) {
        // Try-with-resources: O Scanner será fechado automaticamente ao final
        try (Scanner leitor = new Scanner(System.in)) {

            System.out.println("--- Cadastro de Produtos ---");
            System.out.print("Digite o nome: ");
            String nome = leitor.nextLine();

            System.out.print("Digite o preço: ");
            // Se o usuário digitar "dez", lança InputMismatchException
            double preco = leitor.nextDouble();

            Produto p = new Produto(nome, preco);
            System.out.println("Sucesso! " + p);

        } catch (InputMismatchException e) {
            // Trata erro de entrada (ex: digitar letras em campo numérico)
            System.err.println("Erro de Entrada: Você deve digitar um número para o preço.");

        } catch (ProdutoInvalidoException | IllegalArgumentException e) {
            // Multi-catch: Trata erros de regra de negócio em um único bloco
            System.err.println("Regra Violada: " + e.getMessage());

        } catch (Exception e) {
            // Catch genérico para qualquer outro erro imprevisto (Boas Práticas)
            System.err.println("Erro Inesperado: " + e.getMessage());
            e.printStackTrace(); // Importante para o log do desenvolvedor

        } finally {
            System.out.println("Fim da operação."); // Executa sempre
        }
    }
}
