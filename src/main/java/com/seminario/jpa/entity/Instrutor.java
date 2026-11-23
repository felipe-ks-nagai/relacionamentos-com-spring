package com.seminario.jpa.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "instrutores")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    // lado inverso: o JPA não grava a FK a partir daqui
    @OneToMany(mappedBy = "instrutor")
    private Set<Curso> cursos = new LinkedHashSet<>();

    public Instrutor() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Curso> getCursos() { return cursos; }
    public void setCursos(Set<Curso> cursos) { this.cursos = cursos; }
}
