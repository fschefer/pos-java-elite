package org.unipds;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Garante a ordem para testar o estado
public class UnipdsResourceTest {

    @Test
    @Order(1)
    public void testInitialState() {
        given()
                .when().get("/unipds")
                .then()
                .statusCode(200)
                .body(is("0")); // O contador começa a 0
    }

    @Test
    @Order(2)
    public void testIncrement() {
        given()
                .when().post("/unipds") // Chama o POST para incrementar
                .then()
                .statusCode(204); // No Content (void)

        // Verifica se incrementou
        given()
                .when().get("/unipds")
                .then()
                .statusCode(200)
                .body(is("1"));
    }

    @Test
    @Order(3)
    public void testSetSpecificValue() {
        given()
                .body("10") // Envia "10" no corpo
                .contentType("text/plain")
                .when().post("/unipds/set")
                .then()
                .statusCode(204);

        // Verifica se o valor mudou para 10
        given()
                .when().get("/unipds")
                .then()
                .statusCode(200)
                .body(is("10"));
    }

    @Test
    @Order(4)
    public void testDelete() {
        given()
                .when().delete("/unipds")
                .then()
                .statusCode(204);

        // Verifica se voltou a 0
        given()
                .when().get("/unipds")
                .then()
                .statusCode(200)
                .body(is("0"));
    }

    @Test
    public void testDiferentaoEndpoint() {
        given()
                .when().get("/unipds/diferentao")
                .then()
                .statusCode(200)
                .body(containsString("Retorno Diferente"));
    }
}