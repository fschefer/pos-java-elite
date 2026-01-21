##  Os 4 Pilares da Orientação a Objetos (POO)

A POO é um paradigma que modela sistemas a partir de "objetos", que são coisas da vida real transformadas em código. Segundo o professor Isidro, o grande segredo é que você está **criando novos tipos de dados**.



### 2.1. Abstração 

Abstração é modelar entidades do mundo real selecionando apenas o que é essencial para o sistema.

- **O conceito:** Em um banco, o que importa para a conta é o saldo e o titular. A cor dos olhos do cliente não entra na "abstração" bancária.
    
- **Vantagem:** Reduz a complexidade e permite que o mesmo tipo seja usado em catálogos, estoques ou notas fiscais.
    
- **Implementação Técnica:** ![[projeto-java/src/main/java/fundamentos/oo/Produto.java]]
    

### 2.2. Encapsulamento (A "Blindagem") 

É o pilar que protege os dados internos da classe. O objetivo é evitar que qualquer um mexa nos atributos diretamente.


- **Como funciona:** Usamos o modificador `private` e criamos "portas de entrada" chamadas Getters e Setters.
    
- **A lógica de QA:** Isso blinda o objeto contra estados inválidos (ex: colocar um preço negativo).
    
- **Frameworks:** Para ferramentas como JPA (banco de dados), o encapsulamento é obrigatório para que o sistema funcione corretamente.
    

### 2.3. Herança 

Permite que uma classe nova aproveite tudo que uma classe antiga já tem.

- **O termo:** Em Java, usamos `extends`, que significa **ampliar**.
    
- **A Hierarquia:** A classe base (Pai) é a mais simples, e a classe filha é a mais especializada (tem mais coisas).
    
- **Implementação Técnica (Classe Base):** ![[projeto-java/src/main/java/fundamentos/oo/ContaBancaria.java]]
    

### 2.4. Polimorfismo 

É a capacidade de um mesmo objeto assumir "muitas formas".

- **O funcionamento:** Você dá a mesma ordem (ex: "debitar"), mas cada tipo de conta responde do seu jeito.
    
- **Substituição (Override):** A classe filha redefine um método da classe pai para mudar o comportamento.
    
- **Implementação Técnica (Especialização):** ![[projeto-java/src/main/java/fundamentos/oo/ContaEspecial.java]]
    

---

## 3. Modificadores de Acesso (Os Códigos Secretos)

Os modificadores dizem quem pode entrar na sua "casa" (classe) e mexer nos seus "móveis" (atributos).

1. **`public` (Porta Aberta):** Todo mundo pode ver e mexer, de qualquer lugar.
    
2. **`private` (Cofre Trancado):** Só quem está dentro da mesma classe pode mexer. É a base do encapsulamento.
    
3. **`protected` (Só a Família):** Só as classes filhas (herança) ou quem está na mesma pasta (pacote) pode mexer.
    
4. **Default (Sem nada):** Só quem está na mesma pasta (pacote) pode ver.
    

---

## 4. O Uso do `super` (A Ponte com o Pai)

Quando criamos um "filho" (subclasse), ele precisa avisar o "pai" (superclasse) para ele se organizar primeiro.

- **No Construtor:** O `super(parametros)` deve ser a primeira linha. Ele envia os dados para o pai criar a base da estrutura.
    
- **Nos Métodos:** Usamos `super.metodo()` quando queremos usar a lógica que o pai já tem, antes ou depois de adicionarmos a nossa nova lógica.
    

---

##  5. Records (O Novo Atalho do Java)

Introduzidos nas versões recentes, os **Records** são "classes de dados" imutáveis. Eles são perfeitos para quando você só precisa guardar informações sem mudar nada depois.

- **Vantagem:** O Java cria sozinho o construtor, os getters, o `toString` e o `equals`. Você escreve uma linha e ele faz o trabalho de 50.
    
- **Imutabilidade:** Uma vez criado, você não pode mudar os dados (não existem setters).