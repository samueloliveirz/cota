package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.UsuarioResponse;
import com.samueloliverz.Cota.service.UsuarioLogadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = PainelController.class)
@RequiredArgsConstructor
public class UsuarioLogadoAdvice {

    private final UsuarioLogadoService usuarioLogado;

    @ModelAttribute("usuarioLogado")
    public UsuarioResponse usuarioLogado() {
        return UsuarioResponse.from(usuarioLogado.get());
    }
}