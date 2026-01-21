package fundamentos.apis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionAPI {
    public static void explorar(Object obj) throws Exception {
        Class<?> classe = obj.getClass();
        System.out.println("Explorando a classe: " + classe.getName());

        // Extraindo atributos (mesmo os privados)
        System.out.println("--- Atributos ---");
        for (Field f : classe.getDeclaredFields()) {
            System.out.println("Nome: " + f.getName() + " | Tipo: " + f.getType().getSimpleName());
        }

        // Extraindo métodos
        System.out.println("--- Métodos ---");
        for (Method m : classe.getDeclaredMethods()) {
            System.out.println("Método: " + m.getName() + " | Retorno: " + m.getReturnType().getSimpleName());
        }
    }
}