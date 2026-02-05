package org.unipds;

import io.micrometer.core.annotation.Counted; // Import necessário para a métrica
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/pessoa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PessoaResource {

    // Listar todos (Com métrica de observabilidade)
    @GET
    // A anotação @Counted cria uma métrica customizada que será exposta no Prometheus
    @Counted(value = "counter_get_pessoa", description = "Quantas vezes o metodo listar pessoas foi chamado")
    public List<Pessoa> listar() {
        return Pessoa.listAll();
    }

    // Buscar por ano (Custom Finder)
    @GET
    @Path("/ano/{ano}")
    public List<Pessoa> buscarPorAno(@PathParam("ano") int ano) {
        return Pessoa.findByAnoNascimento(ano);
    }

    // Criar (Transacional)
    @POST
    @Transactional // Obrigatório para operações de escrita (INSERT, UPDATE, DELETE)
    public Pessoa adicionar(Pessoa pessoa) {
        // Boa prática: garantir que o ID é nulo para evitar update acidental
        pessoa.id = null;
        pessoa.persist();
        return pessoa;
    }

    // Atualizar
    @PUT
    @Transactional
    public Pessoa atualizar(Pessoa pessoaRecebida) {
        // Recupera a entidade anexada (managed) do banco
        Pessoa p = Pessoa.findById(pessoaRecebida.id);
        if (p == null) {
            throw new WebApplicationException("Pessoa não encontrada", 404);
        }
        // Atualiza os campos
        p.nome = pessoaRecebida.nome;
        p.anoNascimento = pessoaRecebida.anoNascimento;
        // Não precisa chamar persist(), o JPA detecta alterações na transação (Dirty Checking)
        return p;
    }

    // Deletar
    @DELETE
    @Path("/{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Pessoa.deleteById(id);
    }
}