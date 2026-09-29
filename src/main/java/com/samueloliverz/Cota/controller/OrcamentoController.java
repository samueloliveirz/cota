package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoRequest;
import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrcamentoResponse criar(@Valid @RequestBody OrcamentoRequest request) {
        return OrcamentoResponse.from(service.salvar(request.toEntity()));
    }

    @GetMapping
    public List<OrcamentoResponse> listarTodos() {
        return service.listarTodos().stream().map(OrcamentoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public OrcamentoResponse buscarPorId(@PathVariable Long id) {
        return OrcamentoResponse.from(service.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public OrcamentoResponse mudarStatus(@PathVariable Long id, @RequestParam StatusOrcamento status) {
        return OrcamentoResponse.from(service.mudarStatus(id, status));
    }

    @GetMapping("/cnpj/{cnpj}")
    public List<OrcamentoResponse> historicoCnpj(@PathVariable String cnpj) {
        return service.historicoCnpj(cnpj).stream().map(OrcamentoResponse::from).toList();
    }

    @GetMapping("/cnpj/{cnpj}/recorrente")
    public boolean clienteRecorrente(@PathVariable String cnpj) {
        return service.clienteRecorrente(cnpj);
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@PathVariable Long id, @PathVariable Long itemId) {
        service.removerItem(id, itemId);
    }
}