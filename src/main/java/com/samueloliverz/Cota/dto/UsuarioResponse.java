package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.model.Usuario;

public record UsuarioResponse(
        Long id,
        String username,
        Setor setor,
        Role role
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getSetor(),
                usuario.getRole()
        );
    }
}