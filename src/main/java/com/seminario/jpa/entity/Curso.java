package com.seminario.jpa.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer cargaHoraria;

    // N:1 - Curso é o dono (FK instrutor_id NOT NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    // N:N - lado inverso (a tabela aluno_curso é gravada por Aluno)
    @ManyToMany(mappedBy = "cursos")
    private Set<Aluno> alunos = new LinkedHashSet<>();

    public Curso() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
    public Instrutor getInstrutor() { return instrutor; }
    public void setInstrutor(Instrutor instrutor) { this.instrutor = instrutor; }
    public Set<Aluno> getAlunos() { return alunos; }
    public void setAlunos(Set<Aluno> alunos) { this.alunos = alunos; }
}
