package mx.florinda.networking;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorSocketManual {
    public static void main(String[] args) throws IOException {
        int porta = 8080; // Porta diferente para não conflitar com o outro
        System.out.println("Servidor TCP RAW (Sockets) iniciado na porta " + porta);

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            while (true) {
                // Bloqueia aqui até alguém conectar
                try (Socket cliente = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + cliente.getInetAddress());

                    // Lê o JSON (ou devolve vazio se não existir)
                    String json = Files.exists(Path.of("itens_cardapio.json"))
                            ? Files.readString(Path.of("itens_cardapio.json"))
                            : "{ \"erro\": \"Arquivo nao encontrado\" }";

                    // Escreve a resposta HTTP manualmente
                    PrintStream saida = new PrintStream(cliente.getOutputStream());

                    saida.println("HTTP/1.1 200 OK");
                    saida.println("Content-Type: application/json; charset=utf-8");
                    saida.println("Server: JavaSocketServer/1.0");
                    saida.println(); // Linha em branco obrigatória entre Header e Body
                    saida.println(json);
                }
            }
        }
    }
}