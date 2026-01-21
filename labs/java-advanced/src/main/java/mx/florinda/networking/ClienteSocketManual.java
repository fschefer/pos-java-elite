package mx.florinda.networking;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSocketManual {
    public static void main(String[] args) {
        // Tenta conectar no nosso Servidor Socket (porta 8080)
        try (Socket socket = new Socket("localhost", 8080)) {
            System.out.println("Conectado ao servidor!");

            // Envia requisição manual
            PrintStream saida = new PrintStream(socket.getOutputStream());
            saida.println("GET /itens_cardapio.json HTTP/1.1");
            saida.println("Host: localhost:8080");
            saida.println();

            // Lê a resposta bruta
            Scanner entrada = new Scanner(socket.getInputStream());
            while (entrada.hasNextLine()) {
                System.out.println("[Recebido]: " + entrada.nextLine());
            }

        } catch (IOException e) {
            System.err.println("Erro: O servidor na porta 8080 está rodando?");
        }
    }
}