package mx.florinda.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mx.florinda.cardapio.Database;
import mx.florinda.cardapio.ItemCardapio;

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

        // 3. Persiste no disco na raiz do projeto
        Path path = Path.of("itens_cardapio.json");
        Files.writeString(path, json);

        System.out.println("Arquivo gerado com sucesso em: " + path.toAbsolutePath());
        System.out.println("Conteúdo:\n" + json);
    }
}