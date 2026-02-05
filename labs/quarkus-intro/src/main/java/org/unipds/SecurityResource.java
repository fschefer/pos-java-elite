package org.unipds;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/security")
@RequestScoped // Necessário para injeção do JWT do contexto atual
public class SecurityResource {

    @Inject
    JsonWebToken jwt; // Injeta o token decodificado da requisição atual

    @GET
    @Path("/public")
    @Produces(MediaType.TEXT_PLAIN)
    public String publicEndpoint() {
        return "Acesso Público";
    }

    @GET
    @Path("/user")
    @RolesAllowed("Subscriber") // RBAC: Só permite acesso se o token tiver a role "Subscriber"
    @Produces(MediaType.TEXT_PLAIN)
    public String me() {
        // Acessando claims do token (ex: nome de usuário)
        String userName = jwt.getName();
        return "Olá, " + userName + "! Você tem acesso de Subscriber.";
    }

    @GET
    @Path("/admin")
    @RolesAllowed("Admin") // RBAC: Exige role "Admin"
    @Produces(MediaType.TEXT_PLAIN)
    public String admin() {
        return "Área Administrativa";
    }
}