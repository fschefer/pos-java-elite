package br.com.unipds.reactive.repository;

import br.com.unipds.reactive.model.DocFiscal;
import org.springframework.data.repository.ListCrudRepository;

public interface DocFiscalRepo extends ListCrudRepository<DocFiscal, Integer> {

    // Spring Data cria automaticamente a query SQL: SELECT * FROM tbl_doc_fiscal WHERE protocolo = ?
    DocFiscal findByProtocolo(String protocolo);
}