package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Aluno;

public record AlunoResumoResponse(Long id, String nome, String email) {

    public static AlunoResumoResponse from(Aluno aluno) {
        return new AlunoResumoResponse(aluno.getId(), aluno.getNome(), aluno.getEmail());
    }
}
