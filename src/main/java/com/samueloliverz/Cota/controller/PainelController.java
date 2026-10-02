package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.dto.OrdemManutencaoRequest;
import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.OrdemManutencao;
import com.samueloliverz.Cota.service.OrcamentoService;
import com.samueloliverz.Cota.service.OrdemManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/painel")
@RequiredArgsConstructor
public class PainelController {

    private final OrcamentoService service;
    private final OrdemManutencaoService manutencaoService;

    @GetMapping
    public String menu() {
        return "telas/menu";
    }

    @GetMapping("/orcamentos")
    public String listar(Model model) {
        List<OrcamentoResponse> orcamentos = service.listarTodos().stream()
                .map(OrcamentoResponse::from)
                .sorted(Comparator.comparing(OrcamentoResponse::id).reversed())
                .toList();

        model.addAttribute("orcamentos", orcamentos);
        model.addAttribute("statusList", StatusOrcamento.values());
        return "telas/lista";
    }

    @PostMapping("/orcamentos/{id}/status")
    public String mudarStatus(@PathVariable Long id, @RequestParam StatusOrcamento status) {
        service.mudarStatus(id, status);
        return "redirect:/painel/orcamentos";
    }

    @GetMapping("/orcamentos/novo")
    public String novoOrcamento() {
        return "telas/orcamento-form";
    }

    @GetMapping("/manutencoes/nova")
    public String novaManutencao(Model model) {
        model.addAttribute("produtos", ProdutoManutencao.values());
        return "telas/manutencao-form";
    }

    @PostMapping("/manutencoes")
    public String salvarManutencao(@Valid @ModelAttribute("form") OrdemManutencaoRequest form,
                                   BindingResult resultado,
                                   Model model) {

        if (resultado.hasErrors()) {
            List<String> erros = resultado.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("erros", erros);
            model.addAttribute("produtos", ProdutoManutencao.values());
            return "telas/manutencao-form";
        }

        OrdemManutencao salva = manutencaoService.salvar(form.toEntity());
        return "redirect:/manutencoes/" + salva.getId() + "/pdf";
    }
}