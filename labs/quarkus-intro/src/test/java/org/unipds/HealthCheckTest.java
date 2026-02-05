package org.unipds;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HealthCheckTest {

    @Test
    public void testLivenessProbe() {
        // Verifica se a aplicação está viva
        given()
                .when().get("/q/health/live")
                .then()
                .statusCode(200)
                .body("status", is("UP")); // O status global deve ser UP
    }

    @Test
    public void testReadinessProbe() {
        // Verifica se a aplicação está pronta (depende do Mock ou da integração)
        // Nota: Em testes de integração reais, o estado do Readiness pode variar.
        // Aqui validamos apenas se o endpoint responde.
        given()
                .when().get("/q/health/ready")
                .then()
                .statusCode(200) // 200 se UP, 503 se DOWN
                .body("status", is("UP"));
    }
}