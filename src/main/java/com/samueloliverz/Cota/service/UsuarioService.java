package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.dto.UsuarioRequest;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario criar(UsuarioRequest dados) {
        if (repository.findByUsername(dados.username()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com esse nome");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dados.username());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));
        usuario.setSetor(dados.setor());
        usuario.setRole(dados.role());
        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }
}