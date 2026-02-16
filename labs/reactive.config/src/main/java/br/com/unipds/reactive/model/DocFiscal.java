package br.com.unipds.reactive.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_doc_fiscal")
public class DocFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String protocolo;

    @Column(columnDefinition = "TEXT") // TEXT é útil porque o JSON (documento) pode ser grande
    private String documento;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
}