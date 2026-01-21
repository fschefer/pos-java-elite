## 1. Optional API (Segurança contra Nulos)

Introduzida no Java 8, a API `Optional` é um contêiner (invólucro) que expressa explicitamente que um valor pode ou não estar presente. O objetivo primordial é evitar o famigerado `NullPointerException`.



- **`Optional.of(valor)`**: Use quando tiver certeza absoluta de que o valor não é nulo; caso contrário, lançará erro imediatamente.
    
- **`Optional.ofNullable(valor)`**: A forma mais segura, pois aceita valores nulos sem quebrar, criando uma "caixa vazia".
    
- **Tratamento Fluido**: Em vez de múltiplos blocos `if`, usamos métodos como `.orElse()` para definir um valor padrão ou `.orElseThrow()` para lançar exceções customizadas na mesma linha.
    

**Código:** ![[projeto-java/src/main/java/fundamentos/apis/OptionalAPI.java]]

---

##  2. Date and Time API (`java.time`)

Substitui as antigas APIs (`Date` e `Calendar`) por um modelo moderno, imutável e seguro para threads.



- **Principais Classes**: `LocalDate` (apenas data), `LocalTime` (apenas hora) e `LocalDateTime` (data e hora completas).
    
- **`now()`**: Captura o instante atual do sistema.
    
- **`DateTimeFormatter`**: O "tradutor" essencial para formatar a saída (ex: `dd/MM/yyyy`) ou ler uma data de um texto (`parse`).
    

**Implementação Técnica:** ![[projeto-java/src/main/java/fundamentos/apis/DataHoraAPI.java]]

---

##  3. Reflection & Annotations (O Modo "Hacker")

A API de Reflection permite que o programa examine sua própria estrutura em tempo de execução, enquanto as Anotações servem como "etiquetas" decorativas que orientam comportamentos.

- **Exploração de Metadados**: É possível listar todos os atributos (`Field`) e métodos (`Method`) de um objeto desconhecido.
    
- **Ignorando o Encapsulamento**: Através do método `.setAccessible(true)`, o Java permite acessar e modificar atributos marcados como `private`, um recurso poderoso para ferramentas de QA e frameworks.
    
- **Anotações Customizadas**: Definimos `@interface` para criar etiquetas que podem ser detectadas via Reflection em tempo de execução (`RetentionPolicy.RUNTIME`).
    

**Implementação Técnica:** ![[projeto-java/src/main/java/fundamentos/apis/ReflectionAPI.java]]

---

##  4. Regex (Expressões Regulares)

Uma linguagem de busca sofisticada para validar ou extrair padrões de texto.

- **Validação**: O método `.matches()` verifica se uma String inteira (como um e-mail) segue um padrão específico.
    
- **Extração (`Pattern` & `Matcher`)**: Permite "garimpar" trechos específicos dentro de textos longos, como encontrar um CEP ou um código de erro em um log.
    

---

##  5. Security API (Criptografia)

Permite a proteção de dados sensíveis através de algoritmos de embaralhamento como o **AES**.

- **Cipher**: A classe que realiza a criptografia (`ENCRYPT_MODE`) e a decriptografia (`DECRYPT_MODE`).
    
- **Base64**: Fundamental para transformar o resultado binário da criptografia em um texto que pode ser armazenado em bancos de dados ou arquivos de configuração.