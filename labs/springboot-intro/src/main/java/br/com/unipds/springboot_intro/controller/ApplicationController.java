package br.com.unipds.springboot_intro.controller;

import br.com.unipds.springboot_intro.model.Produto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @GetMapping("/produtos")
    public Produto exibirProduto() {
        Produto p = new Produto();
        p.setId(9876);
        p.setNome("Computador");
        p.setPreco(1500.00);
        return p; // O Jackson converte isso para {"id":9876, "nome":"Computador"...}
    }

    // Exemplo 3: Recebimento de Dados via Body (POST)
    @PostMapping("/produtos")
    public String addNewProduct(@RequestBody Produto p) {
        // @RequestBody é obrigatório para converter o JSON de entrada no objeto 'p'
        System.out.println("Produto Recebido: " + p.getNome() + " - " + p.getPreco());
        return "OK";
    }
}


