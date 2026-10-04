package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.enums.Setor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "Informe o usuário") String username,
        @NotBlank(message = "Informe a senha")
        @Size(min = 6, message = "A senha precisa ter pelo menos 6 caracteres") String senha,
        @NotNull(message = "Informe o setor") Setor setor,
        @NotNull(message = "Informe a role") Role role
) {
}