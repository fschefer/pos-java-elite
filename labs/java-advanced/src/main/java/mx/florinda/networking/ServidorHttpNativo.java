package mx.florinda.networking;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorHttpNativo {
    public static void main(String[] args) throws IOException {
        int porta = 8000;
        // Cria servidor na porta 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        // Define a rota /itens_cardapio.json
        server.createContext("/itens_cardapio.json", exchange -> {
            System.out.println("Recebi uma requisição!");

            if (Files.exists(Path.of("itens_cardapio.json"))) {
                byte[] response = Files.readString(Path.of("itens_cardapio.json")).getBytes();

                // Headers importantes para o navegador entender
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(200, response.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } else {
                String erro = "Arquivo não encontrado. Rode o GeradorJson primeiro.";
                exchange.sendResponseHeaders(404, erro.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(erro.getBytes());
                }
            }
        });

        server.start();
        System.out.println("Servidor rodando em: http://localhost:" + porta + "/itens_cardapio.json");
    }
}