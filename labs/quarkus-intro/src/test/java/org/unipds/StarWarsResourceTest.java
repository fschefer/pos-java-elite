package org.unipds;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class StarWarsResourceTest {

    @InjectMock
    @RestClient
    StarWarsService starWarsService;

    @Test
    public void testStarshipsSuccess() {
        // Simula o sucesso
        Mockito.when(starWarsService.getStarships())
                .thenReturn("{\"count\": 1, \"results\": [{\"name\": \"Millennium Falcon\"}]}");

        given()
                .when().get("/starwars/starships")
                .then()
                .statusCode(200)
                .body(containsString("Millennium Falcon"));
    }

    @Test
    public void testStarshipsFallback() {
        // Simula uma falha (ex: Timeout ou Erro de Rede)
        Mockito.when(starWarsService.getStarships())
                .thenThrow(new RuntimeException("Simulando falha de conexão"));

        given()
                .when().get("/starwars/starships")
                .then()
                .statusCode(200) // O Fallback responde com sucesso (recuperação)
                .body(containsString("indisponível no momento")); // Valida a mensagem do método de fallback
    }
}