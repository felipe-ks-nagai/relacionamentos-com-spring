package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Aluno;
import com.seminario.jpa.entity.Curso;

import java.util.Comparator;
import java.util.List;

public record CursoResponse(
        Long id,
        String nome,
        Integer cargaHoraria,
        InstrutorResumoResponse instrutor,
        List<AlunoResumoResponse> alunos
) {

    public static CursoResponse from(Curso curso) {
        List<AlunoResumoResponse> alunos = curso.getAlunos().stream()
                .sorted(Comparator.comparing(Aluno::getId))
                .map(AlunoResumoResponse::from)
                .toList();
        return new CursoResponse(
                curso.getId(),
                curso.getNome(),
                curso.getCargaHoraria(),
                InstrutorResumoResponse.from(curso.getInstrutor()),
                alunos);
    }
}
