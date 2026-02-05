package org.unipds;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.inject.Inject;

@Readiness // Define que é um check de Readiness
public class ReadinessCheck implements HealthCheck {

    @Inject
    @RestClient
    StarWarsService starWarsService;

    @Override
    public HealthCheckResponse call() {
        try {
            // Tenta acessar o serviço externo
            starWarsService.getStarships();
            return HealthCheckResponse.up("Estou Pronto! Integração OK.");
        } catch (Exception e) {
            // Se der erro, a aplicação não está pronta para receber tráfego
            return HealthCheckResponse.down("Não estou pronto. StarWars API inacessível.");
        }
    }
}
