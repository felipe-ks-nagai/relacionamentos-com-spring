package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Instrutor;

public record InstrutorResumoResponse(Long id, String nome, String email) {

    public static InstrutorResumoResponse from(Instrutor instrutor) {
        return new InstrutorResumoResponse(instrutor.getId(), instrutor.getNome(), instrutor.getEmail());
    }
}
