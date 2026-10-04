package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.UsuarioRequest;
import com.samueloliverz.Cota.dto.UsuarioResponse;
import com.samueloliverz.Cota.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Cadastro de usuários (somente ADMIN)")
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) {
        return UsuarioResponse.from(service.criar(request));
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listarTodos().stream().map(UsuarioResponse::from).toList();
    }
}