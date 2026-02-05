package org.unipds;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@Path("/unipds")
@Produces(MediaType.APPLICATION_JSON) // Define padrão de saída para toda a classe
@Consumes(MediaType.TEXT_PLAIN)       // Define padrão de entrada para toda a classe
public class UnipdsResource {

    // Estado simples em memória para demonstração
    private int i = 0;

    // GET padrão do endpoint /unipds
    @GET
    public int getI() {
        return i;
    }

    // Exemplo de path específico: GET /unipds/diferentao
    @GET
    @Path("/diferentao")
    public String getDiferentao() {
        return "Retorno Diferente: " + java.time.LocalDateTime.now();
    }

    // POST que incrementa o valor (sem retorno no corpo)
    @POST
    public void increment() {
        i++;
    }

    // DELETE que reseta o valor
    @DELETE
    public void remove() {
        i = 0;
    }

    // POST com parâmetro no corpo para definir valor específico
    @POST
    @Path("/set")
    public void setI(String value) {
        this.i = Integer.parseInt(value);
    }
}