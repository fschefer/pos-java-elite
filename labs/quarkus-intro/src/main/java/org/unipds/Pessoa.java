package org.unipds;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Pessoa extends PanacheEntity {

    // Campos públicos! O Panache reescreve o bytecode para adicionar getters/setters
    // implícitos se você não os criar. Pragmático e limpo.
    public String nome;
    public int anoNascimento;

    // Custom Finder (Método Estático na Entidade)
    public static List<Pessoa> findByAnoNascimento(int ano) {
        return find("anoNascimento", ano).list();
    }
}