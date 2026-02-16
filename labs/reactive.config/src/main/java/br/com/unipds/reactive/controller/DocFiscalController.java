package br.com.unipds.reactive.controller;

import br.com.unipds.reactive.dto.ProtocoloDTO;
import br.com.unipds.reactive.dto.RequisicaoDTO;
import br.com.unipds.reactive.model.DocFiscal;
import br.com.unipds.reactive.service.DocFiscalServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DocFiscalController {

    private final DocFiscalServiceImpl service;

    public DocFiscalController(DocFiscalServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/solicitar")
    public Mono<ResponseEntity<ProtocoloDTO>> solicitar(@RequestBody RequisicaoDTO req) {
        // 1. Gera o protocolo imediatamente
        String protocoloGerado = UUID.randomUUID().toString();

        // 2. Aciona o serviço que irá rodar a chamada à Slow API em background
        service.realizarAutorizacaoApiExterna(req.idCliente(), req.idServico(), protocoloGerado);

        // 3. Liberta o cliente instantaneamente devolvendo um 202 (Accepted)
        return Mono.just(
                ResponseEntity.accepted().body(new ProtocoloDTO(protocoloGerado))
        );
    }

    @GetMapping("/consultar/{protocolo}")
    public ResponseEntity<DocFiscal> consultar(@PathVariable String protocolo) {
        DocFiscal doc = service.consultarPorProtocolo(protocolo);

        if (doc != null) {
            return ResponseEntity.ok(doc);
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se a Slow API ainda não tiver concluído
        }
    }
}