
Nestas aulas, descemos do nível mais alto (consumir uma API pronta) até o subsolo da internet (entender como bits trafegam via TCP).

1. **Alto Nível (Client API):** Consumir dados usando `HttpClient` (Java 11+).
    
2. **Médio Nível (HttpServer):** Criar um servidor usando o `com.sun.net.httpserver` embutido no JDK.
    
3. **Baixo Nível (Sockets):** Criar uma conexão TCP pura e "falar" o protocolo HTTP manualmente escrevendo texto na stream.
    

---

## 1. Preparação do Ambiente (Massa de Dados)

Antes de trafegar dados, precisamos **ter** os dados. O professor utilizou o projeto do "Restaurante" para gerar um arquivo JSON que será servido pelos nossos servidores.

### `GeradorJson.java`

Gera o arquivo `itens_cardapio.json` na raiz do projeto usando a biblioteca **Gson**.

Java

```
package mx.florinda.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mx.florinda.cardapio.Database; // Do módulo anterior
import mx.florinda.cardapio.ItemCardapio; // Do módulo anterior

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorJson {
    public static void main(String[] args) throws IOException {
        // 1. Busca os dados do "Banco em Memória"
        Database db = new Database();
        List<ItemCardapio> itens = db.listarTodos();

        // 2. Serializa para JSON (com formatação bonita)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(itens);

        // 3. Persiste no disco usando NIO.2
        Path path = Path.of("itens_cardapio.json");
        Files.writeString(path, json);

        System.out.println("Arquivo gerado com sucesso em: " + path.toAbsolutePath());
        System.out.println("Conteúdo:\n" + json);
    }
}
```

---

##  2. O Lado Cliente (Consumindo APIs)

O Java evoluiu drasticamente aqui.

- **Passado (Legacy):** `java.net.URL` e `HttpURLConnection`. Verboso, bloqueante e difícil de configurar.
    
- **Presente (Moderno):** `java.net.http.HttpClient` (Java 11+). Fluente, assíncrono e suporta HTTP/2.
    

### `ClienteViaCep.java`

Exemplo de como consumir uma API REST externa moderna.

Java

```
package mx.florinda.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteViaCep {
    public static void main(String[] args) {
        // Endpoint público para teste (Praça da Sé)
        String url = "https://viacep.com.br/ws/01001000/json/";

        // 1. Criar o Cliente (O "Navegador")
        // O HttpClient gerencia conexões e pools de threads (Java 11+)
        try (HttpClient client = HttpClient.newHttpClient()) {
            
            // 2. Montar a Requisição (Builder Pattern)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET() // Método HTTP
                    .build();

            System.out.println("Enviando requisição para: " + url);

            // 3. Enviar e receber a Resposta
            // BodyHandlers.ofString() converte os bytes recebidos para String automaticamente
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Processar os dados
            System.out.println("--- Resposta Recebida ---");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Headers: " + response.headers());
            System.out.println("Body:\n" + response.body());

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro na comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## 3. O Lado Servidor (Nível Médio)

A partir do Java 6, o JDK inclui um servidor HTTP leve (`com.sun.net.httpserver`). Ele abstrai a complexidade do TCP, mas ainda exige que configuremos os Headers manualmente.

### `ServidorHttpNativo.java`

Sobe um servidor na porta 8000 que lê o arquivo JSON do disco e entrega via HTTP.

Java

```
package mx.florinda.networking;

import com.sun.net.httpserver.HttpServer; // Pacote interno do JDK (não requer libs externas)

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorHttpNativo {
    public static void main(String[] args) throws IOException {
        int porta = 8000;
        
        // 1. Cria o servidor atrelado à porta 8000
        // O segundo parâmetro (0) é o backlog (fila de espera de conexões padrão)
        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        // 2. Define uma Rota (Endpoint)
        server.createContext("/itens_cardapio.json", exchange -> {
            System.out.println("Recebi uma requisição!");

            // Ler o conteúdo do arquivo
            String json = Files.readString(Path.of("itens_cardapio.json"));
            byte[] respostaBytes = json.getBytes();

            // Configurar Headers (Importante para o navegador entender acentuação e formato)
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            
            // Enviar cabeçalhos: (Status 200 OK, Tamanho do conteúdo)
            exchange.sendResponseHeaders(200, respostaBytes.length);

            // Escrever o corpo da resposta
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(respostaBytes);
            }
        });

        // 3. Inicia o servidor
        server.setExecutor(null); // Usa o executor padrão (single-thread para este exemplo)
        server.start();
        
        System.out.println("Servidor HTTP rodando em http://localhost:" + porta + "/itens_cardapio.json");
    }
}
```

---

##  4. O "Subsolo": Sockets e TCP (Nível Baixo)

Aqui removemos todas as abstrações. HTTP é apenas **texto** enviado através de uma conexão **TCP**.

- **ServerSocket:** Fica ouvindo uma porta.
    
- **Socket:** É o canal de comunicação estabelecido (como uma chamada telefônica).
    
- **Protocolo HTTP:** Nós temos que escrever manualmente `HTTP/1.1 200 OK` para o navegador entender.
    

### `ServidorSocketManual.java`

Um servidor web feito do zero absoluto.

Java

```
package mx.florinda.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorSocketManual {
    public static void main(String[] args) throws IOException {
        int porta = 8000;
        System.out.println("Iniciando servidor TCP RAW na porta " + porta);

        // 1. ServerSocket: Abre a porta e fica escutando
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            while (true) {
                System.out.println("\nEsperando conexão...");
                
                // 2. Accept: BLOQUEIA a execução até um cliente (navegador/curl) conectar
                try (Socket cliente = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + cliente.getInetAddress());

                    // Preparar o conteúdo
                    String json = Files.readString(Path.of("itens_cardapio.json"));

                    // Pegar o canal de saída (Output Stream) para falar com o cliente
                    OutputStream saida = cliente.getOutputStream();
                    PrintStream escritor = new PrintStream(saida);

                    // --- IMPLEMENTAÇÃO MANUAL DO PROTOCOLO HTTP ---
                    
                    // A. Status Line (Versão HTTP + Código de Status)
                    escritor.println("HTTP/1.1 200 OK");
                    
                    // B. Headers (Metadados)
                    escritor.println("Content-Type: application/json; charset=utf-8");
                    escritor.println("Server: JavaSocketServer/1.0 v1");
                    escritor.println("Connection: close"); // Avisa que vamos fechar logo em seguida
                    
                    // C. Linha em Branco (OBRIGATÓRIA: Separa o Header do Body)
                    escritor.println();
                    
                    // D. Body (O conteúdo real)
                    escritor.println(json);
                    
                    System.out.println("Resposta HTTP enviada manualmente.");
                } 
                // O socket do cliente fecha aqui (try-with-resources), encerrando a conexão.
            }
        }
    }
}
```

### `ClienteSocketManual.java`

Um "navegador de terminal" que conecta no servidor acima e lê a resposta bruta.

Java

```
package mx.florinda.networking;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSocketManual {
    public static void main(String[] args) {
        // Conecta no IP e Porta
        try (Socket socket = new Socket("localhost", 8000)) {
            System.out.println("Conectado ao servidor!");

            // 1. Enviar Requisição HTTP (Escrever no OutputStream)
            PrintStream saida = new PrintStream(socket.getOutputStream());
            
            // Simula o que o Chrome/Curl envia
            saida.println("GET /itens_cardapio.json HTTP/1.1");
            saida.println("Host: localhost:8000");
            saida.println(); // Linha em branco finaliza o Request Header
            
            // 2. Ler Resposta (Ler do InputStream)
            Scanner entrada = new Scanner(socket.getInputStream());
            while (entrada.hasNextLine()) {
                String linha = entrada.nextLine();
                System.out.println("[Recebido]: " + linha);
            }

        } catch (IOException e) {
            System.err.println("Erro: Verifique se o servidor está rodando na porta 8000.");
            e.printStackTrace();
        }
    }
}
```

