package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.service.OrcamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/painel")
@RequiredArgsConstructor
public class PainelController {

    private final OrcamentoService service;

    @GetMapping
    public String listar(Model model) {
        List<OrcamentoResponse> orcamentos = service.listarTodos().stream()
                .map(OrcamentoResponse::from)
                .sorted(Comparator.comparing(OrcamentoResponse::id).reversed())
                .toList();

        model.addAttribute("orcamentos", orcamentos);
        return "telas/lista";
    }
}