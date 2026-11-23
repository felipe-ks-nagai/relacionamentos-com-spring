package com.seminario.jpa.dto;

import com.seminario.jpa.entity.Perfil;

public record PerfilResponse(Long id, String telefone, String cidade) {

    public static PerfilResponse from(Perfil perfil) {
        return new PerfilResponse(perfil.getId(), perfil.getTelefone(), perfil.getCidade());
    }
}
