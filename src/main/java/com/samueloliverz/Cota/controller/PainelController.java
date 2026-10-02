package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.dto.OrdemManutencaoRequest;
import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.model.OrdemManutencao;
import com.samueloliverz.Cota.service.OrcamentoService;
import com.samueloliverz.Cota.service.OrdemManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/painel")
@RequiredArgsConstructor
public class PainelController {

    private final OrcamentoService service;
    private final OrdemManutencaoService manutencaoService;

    // ===== LISTA DE ORÇAMENTOS =====
    @GetMapping
    public String listar(Model model) {
        List<OrcamentoResponse> orcamentos = service.listarTodos().stream()
                .map(OrcamentoResponse::from)
                .sorted(Comparator.comparing(OrcamentoResponse::id).reversed())
                .toList();

        model.addAttribute("orcamentos", orcamentos);
        return "telas/lista";
    }

    // ===== MANUTENÇÃO =====

    // 1. Abre o formulário vazio
    @GetMapping("/manutencoes/nova")
    public String novaManutencao(Model model) {
        model.addAttribute("produtos", ProdutoManutencao.values());
        return "telas/manutencao-form";
    }

    // 2. Recebe o formulário preenchido
    @PostMapping("/manutencoes")
    public String salvarManutencao(@Valid @ModelAttribute("form") OrdemManutencaoRequest form,
                                   BindingResult resultado,
                                   Model model) {

        // Se tiver erro de validação, volta pro formulário mostrando as mensagens
        if (resultado.hasErrors()) {
            List<String> erros = resultado.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("erros", erros);
            model.addAttribute("produtos", ProdutoManutencao.values());
            return "telas/manutencao-form";
        }

        // Tudo certo: salva e abre o PDF
        OrdemManutencao salva = manutencaoService.salvar(form.toEntity());
        return "redirect:/manutencoes/" + salva.getId() + "/pdf";
    }
}