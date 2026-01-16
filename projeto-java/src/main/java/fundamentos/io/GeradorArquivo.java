package fundamentos.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeradorArquivo {
    public static void gerar(String nome, int megabytes) throws IOException {
        String linha = "Esta é uma linha de teste para demonstrar a performance de I/O em Java.\n";
        long bytesAlvo = (long) megabytes * 1024 * 1024;
        long bytesAtuais = 0;

        // Uso do try-with-resources para garantir o fechamento do arquivo [cite: 269]
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nome))) {
            while (bytesAtuais < bytesAlvo) {
                writer.write(linha);
                bytesAtuais += linha.getBytes().length;
            }
        }
    }
}