package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Curso;

public record CursoResumoResponse(Long id, String nome, Integer cargaHoraria) {

    public static CursoResumoResponse from(Curso curso) {
        return new CursoResumoResponse(curso.getId(), curso.getNome(), curso.getCargaHoraria());
    }
}
