package fundamentos.apis;

import java.util.Optional;

public class OptionalAPI {

    public static class ExemploOptional {
        public static void testarNullable(String entrada) {
            // Correção: Usar a classe Optional do Java, não o nome da classe externa
            Optional<String> caixa = Optional.ofNullable(entrada);

            // Se estiver presente, imprime. Se não, usa um valor padrão
            System.out.println("Conteúdo: " + caixa.orElse("Valor Padrão (Entrada era nula)"));
        }
    }
}