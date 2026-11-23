package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Curso;
import com.seminario.jpa.entity.Instrutor;

import java.util.Comparator;
import java.util.List;

public record InstrutorResponse(
        Long id,
        String nome,
        String email,
        List<CursoResumoResponse> cursos
) {

    public static InstrutorResponse from(Instrutor instrutor) {
        List<CursoResumoResponse> cursos = instrutor.getCursos().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();
        return new InstrutorResponse(instrutor.getId(), instrutor.getNome(), instrutor.getEmail(), cursos);
    }
}
