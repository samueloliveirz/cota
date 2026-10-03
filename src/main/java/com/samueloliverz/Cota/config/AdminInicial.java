package com.samueloliverz.Cota.config;

import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInicial implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${cota.admin.username}")
    private String username;

    @Value("${cota.admin.senha}")
    private String senha;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setUsername(username);
        admin.setSenha(passwordEncoder.encode(senha));
        admin.setSetor(Setor.HIDRAULICA);
        admin.setRole(Role.ADMIN);
        repository.save(admin);
    }
}