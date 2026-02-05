package org.unipds;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PessoaResourceTest {

    @Test
    @Order(1)
    public void testListarPessoasIniciais() {
        given()
                .when().get("/pessoa")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("nome", hasItems("Elder", "Hugo", "Isidro"));
    }

    @Test
    @Order(2)
    public void testAdicionarPessoa() {
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.nome = "Ana";
        novaPessoa.anoNascimento = 1995;

        given()
                .contentType(ContentType.JSON)
                .body(novaPessoa)
                .when().post("/pessoa")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nome", is("Ana"));
    }

    @Test
    @Order(3)
    public void testBuscarPorAno() {
        given()
                .pathParam("ano", 1979)
                .when().get("/pessoa/ano/{ano}")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].nome", is("Elder"));
    }

    @Test
    @Order(4)
    public void testAtualizarPessoa() {
        // 1. Descobrir o ID do Hugo dinamicamente
        List<Map<String, Object>> pessoas = given()
                .when().get("/pessoa")
                .as(List.class);

        // Filtra na lista para achar o Hugo (Java Stream API)
        Integer idHugo = pessoas.stream()
                .filter(p -> p.get("nome").equals("Hugo"))
                .map(p -> (Integer) p.get("id"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Hugo não encontrado para testes"));

        // 2. Montar o objeto com o ID correto
        Pessoa atualizacao = new Pessoa();
        atualizacao.id = idHugo.longValue(); // Usa o ID real (ex: 51)
        atualizacao.nome = "Hugo Alterado";
        atualizacao.anoNascimento = 2001;

        // 3. Executar o PUT
        given()
                .contentType(ContentType.JSON)
                .body(atualizacao)
                .when().put("/pessoa")
                .then()
                .statusCode(200)
                .body("nome", is("Hugo Alterado"));
    }

    @Test
    @Order(5)
    public void testDeletarPessoa() {
        // 1. Descobrir o ID do Isidro dinamicamente
        List<Map<String, Object>> pessoas = given()
                .when().get("/pessoa")
                .as(List.class);

        Integer idIsidro = pessoas.stream()
                .filter(p -> p.get("nome").equals("Isidro"))
                .map(p -> (Integer) p.get("id"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Isidro não encontrado para testes"));

        // 2. Executar o DELETE com o ID correto
        given()
                .pathParam("id", idIsidro)
                .when().delete("/pessoa/{id}")
                .then()
                .statusCode(204);

        // 3. Verificar se o tamanho diminuiu (Eram 4: Elder, Hugo Alterado, Isidro, Ana -> Agora 3)
        given()
                .when().get("/pessoa")
                .then()
                .statusCode(200)
                .body("size()", is(3));
    }
}