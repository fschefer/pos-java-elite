
## 1. Java IO (A Abordagem Clássica)

Disponível desde o Java 1.0, esta API é baseada no conceito de **Streams (Fluxos)**. Um stream é uma sequência contínua de dados que flui de uma fonte para um destino.



###  Característica Crítica: Bloqueante

O termo "bloqueante" significa que, quando sua aplicação pede para ler um dado do disco, a thread (linha de execução) **para completamente** e fica ociosa esperando o disco girar e entregar o byte. Em servidores com milhares de conexões, isso mata a performance

### 🧱 As Duas Famílias de Classes

O Java IO divide o mundo em dois tipos de dados

1. **Orientado a Bytes (Binários):** Para imagens, PDFs, executáveis.
    
    - Classes principais: `InputStream` e `OutputStream`.
        
    - Exemplo: `FileInputStream` (lê bytes brutos do arquivo).
        
2. **Orientado a Caracteres (Texto):** Para arquivos `.txt`, `.json`, `.xml`. Trata automaticamente a codificação (UTF-8, ISO-8859-1).
    
    - Classes principais: `Reader` e `Writer`.
        
    - Exemplo: `FileReader` (lê caracteres).
        

> **Nota de QA:** Se você ver um desenvolvedor usando `FileInputStream` para ler um arquivo de texto, abra um bug. Isso vai quebrar caracteres acentuados (encoding). O correto é usar `Reader`

---

## ⚡ 2. Java NIO (New IO - Alta Performance)

Introduzida no Java 1.4 para resolver o problema do bloqueio. Ela muda o paradigma de "Fluxos" para **Canais e Buffers**.

### 🔄 Como funciona (A Analogia do Trem)

- **Channel (Canal):** É o trilho. Uma conexão bidirecional com o arquivo ou socket.
    
- **Buffer (Vagão):** É onde os dados são colocados. Você não lê direto do canal; você manda o canal encher o vagão (buffer) e depois lê do vagão.
    
- **Selector (O Manobrista):** Um único componente que monitora vários canais. Se um canal não tem dados prontos, o programa vai fazer outra coisa em vez de travar. Isso permite **Multiplexação**.
    

### 🏎️ Por que é mais rápido?

No teste de benchmark da aula, o NIO (com `FileChannel`) foi muito mais rápido porque ele lê grandes blocos de memória de uma vez (ex: 8KB ou 16KB), enquanto o IO clássico tende a ler byte a byte se não for bem configurado.

---

## 💎 3. Java NIO.2 (A API Moderna de Arquivos)

Lançada no Java 7, focada em usabilidade. O grande astro aqui é a separação entre "o caminho do arquivo" e "o arquivo em si"

### 🗺️ `Path` vs `File`

- **Antigo (`java.io.File`):** Misturava o caminho com o arquivo. Era confuso e tinha métodos com nomes ruins.
    
- **Novo (`java.nio.file.Path`):** Representa apenas o endereço (caminho) no sistema. É agnóstico ao sistema operacional (funciona igual no Linux e no Windows)
    

### 🛠️ A Classe Utilitária `Files`

É uma fábrica de métodos estáticos para operações comuns. Coisas que antes exigiam 10 linhas de código, agora são feitas em uma:

- `Files.copy(...)`
    
- `Files.move(...)`
    
- `Files.delete(...)`
    
- `Files.exists(...)` 
    

### 👁️ WatchService (O Espião)

Uma feature exclusiva do NIO.2 que permite ao Java "vigiar" uma pasta. Se alguém criar, modificar ou deletar um arquivo lá dentro, seu programa é avisado na hora. É a base para ferramentas de _Hot Reload_

---

## 📊 Comparativo Técnico (Resumo da Apostila)

| **Característica** | **Java IO**                                    | **Java NIO**                                      | **Java NIO.2**                                        |
| ------------------ | ---------------------------------------------- | ------------------------------------------------- | ----------------------------------------------------- |
| **Bloqueio**       | Bloqueante (Síncrono)                          | Não Bloqueante (Assíncrono)                       | Misto                                                 |
| **Unidade**        | Streams (Fluxos)                               | Buffers e Channels                                | Path e Files                                          |
| **Complexidade**   | Baixa (Fácil de aprender)                      | Alta (Controle de memória)                        | Média (Abstração alta)                                |
| **Melhor Uso**     | Arquivos pequenos, leitura sequencial simples  | Servidores de rede, arquivos gigantes, alta carga | Scripts de arquivo, cópias, manipulação de diretórios |

---

## ✅ Boas Práticas Oficiais (QA Checklist)

1. **Recursos:** Sempre use o bloco `try-with-resources`. Ele garante que o arquivo será fechado (`.close()`) mesmo se o programa der erro no meio da leitura. Arquivo aberto vaza memória e trava o arquivo no SO
    
2. **Memória:** Evite `Files.readAllLines()` para arquivos que você não sabe o tamanho. Se o arquivo tiver 2GB, vai estourar a memória RAM da JVM (`OutOfMemoryError`). Prefira ler em partes (Streams ou Buffers)
    
3. **Modernidade:** Em novos projetos, force o uso de **NIO.2** (`Path`/`Files`). É mais seguro e tem melhor tratamento de exceções do que o `java.io.File` antigo

### Resumo das Diferenças 

1. **Java IO (`BufferedReader`):**
    
    - **Estilo:** "Canudinho". Você puxa um dado de cada vez.
        
    - **Prós:** Muito fácil de escrever e entender.
        
    - **Contras:** Lento para arquivos grandes; para a execução do programa.
        
2. **Java NIO (`FileChannel`):**
    
    - **Estilo:** "Pá Carregadeira". Você enche um vagão (Buffer) e processa tudo de uma vez.
        
    - **Prós:** Altíssima velocidade; controle total da memória.
        
    - **Contras:** Código mais complexo (precisa gerenciar o buffer).
        
3. **Java NIO.2 (`Files`):**
    
    - **Estilo:** "Mágica". Um comando faz tudo.
        
    - **Prós:** Código limpo e moderno. Ótimo para scripts e arquivos pequenos/médios.
        
    - **Contras:** `readAllLines` consome muita memória RAM. Para arquivos grandes, prefira usar `Files.newBufferedReader` (que volta a usar Streams) ou Channels.