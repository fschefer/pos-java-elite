package br.com.unipds.reactive.service;

import br.com.unipds.reactive.model.DocFiscal;
import br.com.unipds.reactive.repository.DocFiscalRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DocFiscalServiceImpl {

    private final DocFiscalRepo repository;
    private final WebClient webClient;

    public DocFiscalServiceImpl(DocFiscalRepo repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public void realizarAutorizacaoApiExterna(Long idCliente, Integer idServico, String protocolo) {
        String uri = "https://slowapi.easylab.com.br/api/v1/autorizacao/" + idCliente + "?servico=" + idServico;

        // Requisição reativa não-bloqueante
        webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class) // Promessa de retornar 1 String
                .doOnNext(respostaExterna -> { // O que fazer em caso de Sucesso
                    System.out.println("Solicitação atendida pela API Externa para o protocolo " + protocolo);

                    DocFiscal doc = new DocFiscal();
                    doc.setProtocolo(protocolo);
                    doc.setDocumento(respostaExterna);

                    repository.save(doc); // Persiste assincronamente no DB
                })
                .doOnError(erro -> { // O que fazer em caso de Falha
                    System.err.println("Erro ao chamar a Slow API: " + erro.getMessage());
                })
                .subscribe(); // Dispara o fluxo para a execução em background!
    }

    public DocFiscal consultarPorProtocolo(String protocolo) {
        return repository.findByProtocolo(protocolo);
    }
}