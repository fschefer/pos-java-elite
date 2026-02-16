package br.com.unipds.reactive.controller;

import br.com.unipds.reactive.dto.RequisicaoDTO;
import br.com.unipds.reactive.model.DocFiscal;
import br.com.unipds.reactive.service.DocFiscalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

// 1. Usamos @WebFluxTest em vez de @WebMvcTest para controladores reativos
@WebFluxTest(controllers = DocFiscalController.class)
public class DocFiscalControllerTest {

    // 2. Injetamos o WebTestClient (o substituto reativo do MockMvc)
    @Autowired
    private WebTestClient webTestClient;

    // 3. Mockamos o serviço usando a anotação para Spring Boot 3.4+
    @MockitoBean
    private DocFiscalServiceImpl service;

    @Test
    public void deveRetornar202AcceptedEProtocoloAoSolictar() {
        RequisicaoDTO req = new RequisicaoDTO(123L, 999);

        // Como o método realizarAutorizacaoApiExterna retorna 'void', usamos o doNothing()
        // O ArgumentMatchers.any(String.class) serve para aceitar o UUID gerado aleatoriamente no Controller
        doNothing().when(service).realizarAutorizacaoApiExterna(eq(123L), eq(999), any(String.class));

        webTestClient.post()
                .uri("/api/solicitar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req) // Passamos o body diretamente de forma mais limpa que no MockMvc
                .exchange() // Executa a requisição
                .expectStatus().isAccepted() // Espera o status 202
                .expectBody()
                .jsonPath("$.protocolo").isNotEmpty(); // Garante que devolveu um protocolo não vazio
    }

    @Test
    public void deveRetornar200EDocumentoAoConsultarProtocoloPronto() {
        DocFiscal docFake = new DocFiscal();
        docFake.setProtocolo("12345-abcde");
        docFake.setDocumento("{\"status\": \"autorizado\", \"codigo\": \"XYZ\"}");

        // Simulamos que a Slow API já terminou e o documento já está no banco
        when(service.consultarPorProtocolo("12345-abcde")).thenReturn(docFake);

        webTestClient.get()
                .uri("/api/consultar/12345-abcde")
                .exchange()
                .expectStatus().isOk() // Espera o status 200
                .expectBody()
                .jsonPath("$.protocolo").isEqualTo("12345-abcde")
                .jsonPath("$.documento").isEqualTo("{\"status\": \"autorizado\", \"codigo\": \"XYZ\"}");
    }

    @Test
    public void deveRetornar404QuandoProtocoloAindaNaoEstiverPronto() {
        // Simulamos que a Slow API ainda está a processar e não guardou nada na base de dados
        when(service.consultarPorProtocolo("99999-xxxxx")).thenReturn(null);

        webTestClient.get()
                .uri("/api/consultar/99999-xxxxx")
                .exchange()
                .expectStatus().isNotFound() // Espera o status 404 (Not Found)
                .expectBody().isEmpty(); // Valida que não devolvemos nenhum corpo
    }
}