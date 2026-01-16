package fundamentos.streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConcessionariaVirtual {
    public static void main(String[] args) {
        // 1. Criação da Massa de Dados
        List<Veiculo> frota = new ArrayList<>();
        frota.add(new Veiculo("Chevrolet Corsa", "Cinza", 25000, 1998, 160));
        frota.add(new Veiculo("Toyota Corolla", "Prata", 70000, 2015, 200));
        frota.add(new Veiculo("Toyota Corolla", "Preto", 100000, 2025, 210));
        frota.add(new Veiculo("BMW X1", "Branca", 250000, 2023, 250));
        frota.add(new Veiculo("Mercedes GLA 200", "Prata", 300000, 2025, 280));

        System.out.println("--- 1. Iteração Simples (forEach) ---");
        frota.stream().forEach(v -> System.out.println(v)); //

        System.out.println("\n--- 2. Ordenação por Preço (do mais caro para o mais barato) ---");
        List<Veiculo> ordenados = frota.stream()
                .sorted(Comparator.comparing(Veiculo::getPreco).reversed()) //
                .collect(Collectors.toList()); // Lista mutável

        ordenados.forEach(System.out::println);

        System.out.println("\n--- 3. Filtrando apenas 'Corolla' ---");
        List<Veiculo> corollas = frota.stream()
                .filter(v -> v.getMarca().contains("Corolla")) //
                .toList(); // Lista imutável (Java 16+)
        corollas.forEach(System.out::println);

        System.out.println("\n--- 4. Estatísticas (Média de Preço dos Corollas) ---");
        double mediaCorolla = frota.stream()
                .filter(v -> v.getMarca().contains("Corolla"))
                .mapToDouble(Veiculo::getPreco) // Transforma Stream<Veiculo> em DoubleStream
                .average()
                .orElse(0.0); // Evita erro se não houver Corollas

        System.out.printf("Preço Médio dos Corollas: R$ %.2f%n", mediaCorolla);

        System.out.println("\n--- 5. Transformação (Map) - Convertendo para Maiúsculas ---");
        frota.stream()
                .filter(v -> v.getMarca().contains("Corolla"))
                .map(ConcessionariaVirtual::converterParaMaiusculo) // Aplica conversão em cada item
                .forEach(System.out::println);
    }

    // Método auxiliar para transformação
    private static Veiculo converterParaMaiusculo(Veiculo v) {
        return new Veiculo(
                v.getMarca().toUpperCase(),
                v.getCor().toUpperCase(),
                v.getPreco(),
                v.getAno(),
                v.getVelocidadeMaxima()
        );
    }
}