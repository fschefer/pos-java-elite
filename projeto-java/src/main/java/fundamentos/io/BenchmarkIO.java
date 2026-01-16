package fundamentos.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;

public class BenchmarkIO {
    public static void main(String[] args) {
        String nomeArquivo = "benchmark_teste.txt";

        try {
            // 0. Preparação: Gera um arquivo de 100MB para o teste
            System.out.println("Gerando arquivo de teste...");
            GeradorArquivo.gerar(nomeArquivo, 100);
            Path path = Paths.get(nomeArquivo);

            // --- FORMA 1: Java IO (Clássico/Bloqueante) ---
            // Baseado em Streams. Lê caractere por caractere ou linha por linha.
            // É simples, mas a thread fica travada esperando o disco.
            long inicio = System.currentTimeMillis();
            try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
                while (br.ready()) {
                    br.readLine(); // Apenas consome a linha
                }
            }
            long fim = System.currentTimeMillis();
            System.out.println("1. Java IO (BufferedReader): " + (fim - inicio) + "ms");

            // --- FORMA 2: Java NIO (Channels & Buffers) ---
            // Baseado em Canais e Buffers. Permite ler grandes blocos de bytes de uma vez.
            // É não-bloqueante e muito mais performático para grandes volumes.
            inicio = System.currentTimeMillis();
            try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocate(16384); // Buffer de 16KB
                while (channel.read(buffer) > 0) {
                    buffer.flip(); // Prepara o buffer para leitura
                    buffer.clear(); // Limpa para a próxima escrita do canal
                }
            }
            fim = System.currentTimeMillis();
            System.out.println("2. Java NIO (FileChannel): " + (fim - inicio) + "ms");

            // --- FORMA 3: Java NIO.2 (Moderno/Simplificado) ---
            // Focado em usabilidade (Files e Path). O método readAllLines é prático,
            // mas perigoso pois carrega tudo na memória RAM.
            inicio = System.currentTimeMillis();
            // Cuidado: Pode causar OutOfMemoryError em arquivos gigantes!
            List<String> linhas = Files.readAllLines(path);
            fim = System.currentTimeMillis();
            System.out.println("3. Java NIO.2 (Files.readAllLines): " + (fim - inicio) + "ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}