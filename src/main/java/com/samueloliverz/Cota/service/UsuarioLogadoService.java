package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioLogadoService {

    private final UsuarioRepository repository;

    public Usuario get() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado"));
    }

    public boolean isAdmin() {
        return get().getRole() == Role.ADMIN;
    }
}