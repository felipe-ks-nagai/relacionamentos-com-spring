package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Aluno;
import com.seminario.jpa.entity.Curso;

import java.util.Comparator;
import java.util.List;

public record AlunoResponse(
        Long id,
        String nome,
        String email,
        PerfilResponse perfil,
        List<CursoResumoResponse> cursos
) {

    public static AlunoResponse from(Aluno aluno) {
        PerfilResponse perfil = aluno.getPerfil() == null ? null : PerfilResponse.from(aluno.getPerfil());
        List<CursoResumoResponse> cursos = aluno.getCursos().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();
        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getEmail(), perfil, cursos);
    }
}
