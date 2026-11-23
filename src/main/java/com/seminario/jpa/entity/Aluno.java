package com.seminario.jpa.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    // 1:1 - Aluno é o dono (FK perfil_id com UNIQUE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", unique = true)
    private Perfil perfil;

    // N:N - Aluno é o dono (define a @JoinTable)
    @ManyToMany
    @JoinTable(
            name = "aluno_curso",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos = new LinkedHashSet<>();

    public Aluno() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public Set<Curso> getCursos() { return cursos; }
    public void setCursos(Set<Curso> cursos) { this.cursos = cursos; }
}
