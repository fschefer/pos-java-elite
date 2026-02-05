package org.unipds;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// Define a URL base da API externa
@RegisterRestClient(baseUri = "https://swapi.py4e.com/api")
public interface StarWarsService {

    @GET
    @Path("/starships") // Endpoint específico da API externa
    @Produces(MediaType.APPLICATION_JSON)
    String getStarships(); // Retorna o JSON cru como String por enquanto
}
