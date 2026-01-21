package mx.florinda.networking;

import com.google.gson.Gson;
import mx.florinda.cardapio.Database;
import mx.florinda.cardapio.ItemCardapio;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorSocketManual {

    // Instância única do banco de dados compartilhada entre as threads
    private static final Database db = new Database();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        int porta = 8080;
        System.out.println("🔥 Servidor TCP Multithread ouvindo na porta " + porta);

        // THREAD POOL: Limita a 50 threads simultâneas para proteger o servidor
        try (ExecutorService pool = Executors.newFixedThreadPool(50);
             ServerSocket serverSocket = new ServerSocket(porta)) {

            while (true) {
                // Aceita conexão (Bloqueante)
                Socket cliente = serverSocket.accept();

                // Delega o processamento para a Thread Pool
                pool.execute(() -> trataRequisicao(cliente));
            }
        }
    }

    private static void trataRequisicao(Socket cliente) {
        // Try-with-resources garante que o socket será fechado ao final
        try (cliente;
             Scanner entrada = new Scanner(cliente.getInputStream());
             PrintStream saida = new PrintStream(cliente.getOutputStream())) {

            if (!entrada.hasNextLine()) return;

            // --- 1. PARSE DA REQUEST LINE ---
            // Ex: "GET /itens-cardapio HTTP/1.1"
            String requestLine = entrada.nextLine();
            String[] parts = requestLine.split(" ");
            String metodo = parts[0]; // GET ou POST
            String path = parts[1];   // /itens-cardapio

            System.out.println("THREAD [" + Thread.currentThread().getName() + "] processando: " + requestLine);

            // Consumir Cabeçalhos (Headers) até a linha em branco
            while (entrada.hasNextLine()) {
                String linha = entrada.nextLine();
                if (linha.isEmpty()) break; // Fim dos headers
            }

            // --- 2. ROTEAMENTO ---

            // Rota 1: Listar todos (GET)
            if (metodo.equals("GET") && path.equals("/itens-cardapio")) {
                responderJson(saida, 200, gson.toJson(db.listarTodos()));

                // Rota 2: Total de itens (GET)
            } else if (metodo.equals("GET") && path.equals("/itens-cardapio/total")) {
                responderJson(saida, 200, "{ \"total\": " + db.totalItens() + " }");

                // Rota 3: Adicionar item (POST)
            } else if (metodo.equals("POST") && path.equals("/itens-cardapio")) {
                // Ler o Body (JSON) caractere por caractere ou bloco
                // Hack simples: Lê tokens até encontrar o fechamento do JSON '}'
                StringBuilder body = new StringBuilder();
                while (entrada.hasNext()) {
                    String token = entrada.next();
                    body.append(token).append(" "); // remontando com espaços
                    if (token.endsWith("}")) break;
                }

                try {
                    ItemCardapio novoItem = gson.fromJson(body.toString(), ItemCardapio.class);
                    db.adicionarItem(novoItem);
                    responderJson(saida, 201, "{ \"status\": \"Criado\", \"id\": " + novoItem.id() + " }");
                } catch (Exception e) {
                    responderJson(saida, 400, "{ \"erro\": \"JSON Invalido\" }");
                }

                // Rota 4: 404 Not Found
            } else {
                responderJson(saida, 404, "{ \"erro\": \"Rota nao encontrada\" }");
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        }
    }

    private static void responderJson(PrintStream saida, int statusCode, String json) {
        String statusMsg = (statusCode == 200) ? "OK" : (statusCode == 201 ? "Created" : "Error");

        saida.println("HTTP/1.1 " + statusCode + " " + statusMsg);
        saida.println("Content-Type: application/json; charset=utf-8");
        saida.println("Connection: close");
        saida.println(); // Linha em branco obrigatória entre headers e body
        saida.println(json);
    }
}