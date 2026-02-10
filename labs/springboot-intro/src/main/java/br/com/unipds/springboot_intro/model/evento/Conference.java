package br.com.unipds.springboot_intro.model.evento;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_id")
    private Integer id;

    @Column(name = "conference_name")
    private String name;

    @Column(name = "conference_address")
    private String address;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}