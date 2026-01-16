package fundamentos.apis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHoraAPI {
    public static void demonstrar() {
        // Pegando a data atual
        LocalDate hoje = LocalDate.now();

        // Formatando a saída
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Data formatada: " + hoje.format(formatter));

        // Lendo uma data de um texto (Parse)
        String dataTexto = "20/01/2026";
        LocalDate dataConvertida = LocalDate.parse(dataTexto, formatter);
        System.out.println("Data convertida: " + dataConvertida);
    }
}
