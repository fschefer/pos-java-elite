package br.com.unipds.springboot_intro.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
    @RequestMapping("/produtos") // Prefixo global para todos os endpoints desta classe
    public class ProdutoController {

        private List<Produto> database;

        // Construtor inicializando o "banco de dados" em memória
        public ProdutoController() {
            this.database = new ArrayList<>();
            this.database.add(new Produto(1, "Computador", 1500.0));
            this.database.add(new Produto(2, "Mouse", 50.0));
            this.database.add(new Produto(3, "Teclado", 100.0));
            this.database.add(new Produto(4, "Monitor", 500.0));
            this.database.add(new Produto(5, "Impressora", 350.0));
        }

        // GET /produtos - Retorna todos
        @GetMapping
        public List<Produto> recuperarTodos() {
            return this.database;
        }

        // GET /produtos/{id} - Busca um específico (com tratamento 404)
        @GetMapping("/{id}")
        public ResponseEntity<Produto> recuperarPeloId(@PathVariable int id) {
            Produto prod = database.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (prod != null) {
                return ResponseEntity.ok(prod); // 200 OK
            }
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // POST /produtos - Cria um novo produto
        @PostMapping
        public ResponseEntity<Produto> adicionar(@RequestBody Produto novo) {
            // Na vida real, o ID seria gerado automaticamente pelo banco
            this.database.add(novo);
            return ResponseEntity.status(201).body(novo); // 201 Created
        }

        // PUT /produtos/{id} - Atualiza um produto (Substituição Total)
        @PutMapping("/{id}")
        public ResponseEntity<Produto> atualizar(@PathVariable int id, @RequestBody Produto dadosNovos) {
            // Busca o índice do produto na lista
            int indice = -1;
            for (int i = 0; i < database.size(); i++) {
                if (database.get(i).getId() == id) {
                    indice = i;
                    break;
                }
            }

            if (indice >= 0) {
                // Garante que o ID do objeto atualizado seja o mesmo da URL
                dadosNovos.setId(id);
                database.set(indice, dadosNovos);
                return ResponseEntity.ok(dadosNovos);
            }
            return ResponseEntity.notFound().build();
        }

        // DELETE /produtos/{id} - Remove um produto
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> remover(@PathVariable int id) {
            boolean removeu = database.removeIf(p -> p.getId() == id);

            if (removeu) {
                return ResponseEntity.ok().build(); // ou ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }

        // GET /produtos/sort?order=asc ou desc - Exemplo de RequestParam
        @GetMapping("/sort")
        public ResponseEntity<List<Produto>> ordenar(@RequestParam(required = false) String order) {
            if (order == null) {
                return ResponseEntity.ok(this.database);
            }

            if (order.equalsIgnoreCase("asc")) {
                List<Produto> ordenada = database.stream()
                        .sorted(Comparator.comparing(Produto::getPreco))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(ordenada);
            } else if (order.equalsIgnoreCase("desc")) {
                List<Produto> ordenada = database.stream()
                        .sorted(Comparator.comparing(Produto::getPreco).reversed())
                        .collect(Collectors.toList());
                return ResponseEntity.ok(ordenada);
            }

            return ResponseEntity.badRequest().build(); // 400 se o parâmetro for inválido
        }
    }


