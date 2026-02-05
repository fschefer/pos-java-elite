package org.unipds;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class SecurityResourceTest {

    @Test
    public void testPublicEndpoint() {
        given()
                .when().get("/security/public")
                .then()
                .statusCode(200)
                .body(is("Acesso Público"));
    }

    @Test
    public void testUserEndpointWithoutToken() {
        given()
                .when().get("/security/user")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "joao@email.com", roles = {"Subscriber"})
    @JwtSecurity(claims = { // Força a criação de um JWT com a claim "upn" ou "preferred_username"
            @Claim(key = "upn", value = "joao@email.com")
    })
    public void testUserEndpointWithCorrectRole() {
        given()
                .when().get("/security/user")
                .then()
                .statusCode(200)
                .body(containsString("Olá, joao@email.com"));
    }

    @Test
    @TestSecurity(user = "maria@email.com", roles = {"Subscriber"})
    public void testAdminEndpointWithIncorrectRole() {
        given()
                .when().get("/security/admin")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin@email.com", roles = {"Admin"})
    public void testAdminEndpointWithCorrectRole() {
        given()
                .when().get("/security/admin")
                .then()
                .statusCode(200)
                .body(is("Área Administrativa"));
    }
}