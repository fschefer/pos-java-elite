package org.unipds;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

    @Liveness // Define que é um check de Liveness
    public class LivenessCheck implements HealthCheck {

        @Override
        public HealthCheckResponse call() {
            return HealthCheckResponse.up("Estou Vivo!");
        }
    }

