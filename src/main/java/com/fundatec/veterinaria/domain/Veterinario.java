package com.fundatec.veterinaria.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "veterinario")
public class Veterinario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;


    @Column(name = "CPF", unique = true)
    private String cpf;

    @OneToMany(
            mappedBy = "veterinario",
            targetEntity = Cachorro.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private List<Cachorro> cachorros;

    public Veterinario() {
    }

    public Veterinario(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Cachorro> getCachorros() {
        return cachorros;
    }

    public void setCachorros(List<Cachorro> cachorros) {
        this.cachorros = cachorros;
    }
}
