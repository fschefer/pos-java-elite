package mx.florinda.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteViaCep {
    public static void main(String[] args) {
        // CEP da Praça da Sé para teste
        String url = "https://viacep.com.br/ws/01001000/json/";

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            System.out.println("Enviando requisição para: " + url);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Body:\n" + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}