package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.dto.OrdemManutencaoResponse;
import com.samueloliverz.Cota.dto.UsuarioRequest;
import com.samueloliverz.Cota.dto.UsuarioResponse;
import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.service.OrcamentoService;
import com.samueloliverz.Cota.service.OrdemManutencaoService;
import com.samueloliverz.Cota.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/painel")
@RequiredArgsConstructor
public class PainelController {

    private static final int ITENS_POR_PAGINA = 10;

    private final OrcamentoService service;
    private final OrdemManutencaoService manutencaoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String inicio() {
        return "redirect:/painel/orcamentos";
    }

    @GetMapping("/orcamentos")
    public String listar(@RequestParam(required = false) String empresa,
                         @RequestParam(required = false) StatusOrcamento status,
                         @RequestParam(defaultValue = "0") int pagina,
                         Model model) {
        Pageable pageable = PageRequest.of(pagina, ITENS_POR_PAGINA, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrcamentoResponse> orcamentos = service.buscar(empresa, status, pageable).map(OrcamentoResponse::from);

        model.addAttribute("orcamentos", orcamentos);
        model.addAttribute("contagem", service.contarPorStatus());
        model.addAttribute("statusList", StatusOrcamento.values());
        model.addAttribute("empresa", empresa);
        model.addAttribute("statusAtual", status);
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

    @GetMapping("/orcamentos/{id}/editar")
    public String editarOrcamento(@PathVariable Long id, Model model) {
        Orcamento orcamento = service.buscarPorId(id);

        if (orcamento.getStatus() == StatusOrcamento.FECHADO) {
            return "redirect:/painel/orcamentos";
        }

        model.addAttribute("orcamentoId", id);
        return "telas/orcamento-form";
    }

    @GetMapping("/manutencoes")
    public String listarManutencoes(@RequestParam(required = false) String empresa,
                                    @RequestParam(defaultValue = "0") int pagina,
                                    Model model) {
        Pageable pageable = PageRequest.of(pagina, ITENS_POR_PAGINA, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrdemManutencaoResponse> ordens = manutencaoService.buscar(empresa, pageable)
                .map(OrdemManutencaoResponse::from);

        model.addAttribute("ordens", ordens);
        model.addAttribute("empresa", empresa);
        return "telas/manutencoes";
    }

    @GetMapping("/manutencoes/nova")
    public String novaManutencao(Model model) {
        model.addAttribute("produtos", ProdutoManutencao.values());
        return "telas/manutencao-form";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos().stream()
                .map(UsuarioResponse::from)
                .sorted(Comparator.comparing(UsuarioResponse::id))
                .toList();

        model.addAttribute("usuarios", usuarios);
        return "telas/usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario() {
        return "telas/usuario-form";
    }

    @PostMapping("/usuarios")
    public String salvarUsuario(@Valid @ModelAttribute("form") UsuarioRequest form,
                                BindingResult resultado,
                                Model model,
                                RedirectAttributes redirect) {

        if (resultado.hasErrors()) {
            model.addAttribute("erros", mensagens(resultado));
            return "telas/usuario-form";
        }

        try {
            Usuario criado = usuarioService.criar(form);
            redirect.addFlashAttribute("criado", criado.getUsername());
            return "redirect:/painel/usuarios/novo";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erros", List.of(e.getMessage()));
            return "telas/usuario-form";
        }
    }

    private List<String> mensagens(BindingResult resultado) {
        return resultado.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }
}