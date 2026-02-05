package org.unipds;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.temporal.ChronoUnit;

@Path("/starwars")
public class StarWarsResource {

    @Inject
    @RestClient
    StarWarsService starWarsService;

    @GET
    @Path("/starships")
    @Produces(MediaType.APPLICATION_JSON)
    // 1. Timeout: Se demorar mais de 3s, falha.
    @Timeout(value = 3, unit = ChronoUnit.SECONDS)
    // 2. Fallback: Se der erro (Timeout ou Exception), chama 'fallback'.
    @Fallback(fallbackMethod = "getStarshipsFallback")
    // 3. Circuit Breaker: Analisa 2 requests; se 50% falhar, abre o circuito por 3s.
    @CircuitBreaker(
            requestVolumeThreshold = 2,
            failureRatio = 0.5,
            delay = 3000,
            successThreshold = 2
    )
    public String getStarships() {
        return starWarsService.getStarships();
    }

    // Método de Fallback (Deve ter a mesma assinatura do original)
    public String getStarshipsFallback() {
        return "{\"message\": \"Serviço StarWars indisponível no momento. Tente mais tarde.\"}";
    }
}
