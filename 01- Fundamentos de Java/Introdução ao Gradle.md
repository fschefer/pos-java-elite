## A Evolução das Ferramentas de Build

Para entender o Gradle, precisamos entender o que veio antes e quais problemas cada ferramenta tentou resolver.

### 1. Ant (2000) - "A Era do Script"

- **Formato:** XML (`build.xml`).
    
- **Filosofia:** Imperativo/Programático. Você descreve o _passo a passo_ exato do que deve ser feito (ex: crie a pasta `classes`, depois compile `src` para `classes`, depois empacote em `jar`).
    
- **Problema:** Não tinha gerenciamento de dependências nativo (precisava do Ivy). O reuso de código entre projetos era difícil ("Copy & Paste hell").
    

### 2. Maven (2004) - "A Era da Padronização"

- **Formato:** XML (`pom.xml` - Project Object Model).
    
- **Filosofia:** Declarativo e Opiniático. "Convenção sobre Configuração".
    
- **Inovação:** Trouxe o **Gerenciamento de Dependências** (Maven Central) e uma estrutura de pastas padrão (`src/main/java`).
    
- **Problema:** XML é verboso. Personalizar o build (fazer algo fora do padrão, como lógica condicional) é muito difícil e exige a criação de plugins complexos.
    

### 3. Gradle (2007) - "O Melhor dos Dois Mundos"

- **Formato:** DSL baseada em **Groovy** ou **Kotlin** (`build.gradle` ou `build.gradle.kts`).
    
- **Filosofia:** Combina a flexibilidade de script do Ant com as convenções e dependências do Maven.
    
- **Vantagens:**
    
    - **Performance:** Builds incrementais (só recompila o que mudou).
        
    - **Flexibilidade:** Como é código de verdade, você pode colocar lógica (`if`, `for`, variáveis) direto no script de build.
        
    - **Padrão Atual:** É a ferramenta oficial do Android e muito forte em microsserviços modernos.
        

---

##  O Ecossistema Gradle

### Instalação e o "Wrapper"

Não é recomendado instalar o Gradle globalmente na máquina. A boa prática é usar o **Gradle Wrapper**:

- **Comando:** `./gradlew build` (Linux/Mac) ou `gradlew.bat build` (Windows).
    
- **Vantagem:** O script baixa a versão exata do Gradle que o projeto precisa, garantindo que todo mundo na equipe rode com a mesma versão.
    

### Estrutura de Arquivos

1. **`build.gradle`**: O coração do projeto. Define plugins, dependências e tarefas.
    
2. **`settings.gradle`**: Define o nome do projeto e módulos (subprojetos).
    

---

##  Laboratório Prático: "Restaurante da Dona Florinda"

Nesta aula, foi criado um projeto para modelar um cardápio, utilizando **Java 21**, **Records** e a biblioteca **Gson** para JSON.

### 1. Configuração (`build.gradle`)

Aqui vemos como o Gradle é mais enxuto que o Maven. Note a dependência do `Gson`.



```
plugins {
    id 'java' // Adiciona tasks como compileJava, test, jar
}

group = 'mx.florinda'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral() // Reusa o ecossistema do Maven
}

dependencies {
    // Declaração limpa da dependência
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // JUnit 5 para testes
    testImplementation platform('org.junit:junit-bom:5.9.1')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}

test {
    useJUnitPlatform()
}
```

### 2. O Domínio (Usando Records)

O professor utilizou `record` (Java 14+) para criar classes de dados imutáveis de forma concisa, substituindo os POJOs cheios de Getters/Setters.

**Arquivo:** `src/main/java/mx/florinda/cardapio/CategoriaCardapio.java`

Java

```
package mx.florinda.cardapio;

public enum CategoriaCardapio {
    ENTRADAS,
    PRATOS_PRINCIPAIS,
    BEBIDAS,
    SOBREMESAS
}
```

**Arquivo:** `src/main/java/mx/florinda/cardapio/ItemCardapio.java`

Java

```
package mx.florinda.cardapio;

import java.math.BigDecimal;

// Record: Gera automaticamente construtor, getters, equals, hashCode e toString
public record ItemCardapio(
    String id,
    String nome,
    String descricao,
    CategoriaCardapio categoria,
    BigDecimal preco,
    BigDecimal precoComDesconto
) {
    // Records permitem construtores compactos ou validações se necessário
}
```

### 3. A Aplicação (Serialização JSON)

Uso do **Text Block** (Java 15+) e da biblioteca Gson para transformar o objeto em JSON.

**Arquivo:** `src/main/java/mx/florinda/cardapio/Main.java`

Java

```
package mx.florinda.cardapio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;

// Import estático para deixar o código mais limpo ao usar o Enum
import static mx.florinda.cardapio.CategoriaCardapio.BEBIDAS;

public class Main {
    public static void main(String[] args) {
        
        // Text Block: String multilinhas formatada
        var descricaoRefresco = """
                Suco de limão que parece tamarindo
                e tem gosto de groselha.
                """;

        var refrescoChaves = new ItemCardapio(
                "1",
                "Refresco do Chaves",
                descricaoRefresco,
                BEBIDAS,
                new BigDecimal("2.99"),
                null // Sem desconto
        );

        // Serialização com Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(refrescoChaves);

        System.out.println("--- Item do Cardápio (JSON) ---");
        System.out.println(json);
    }
}
```

### 4. Saída no Console

Ao rodar `./gradlew run` (ou pela IDE), o resultado seria:

JSON

```
{
  "id": "1",
  "nome": "Refresco do Chaves",
  "descricao": "Suco de limão que parece tamarindo\ne tem gosto de groselha.\n",
  "categoria": "BEBIDAS",
  "preco": 2.99
}
```
