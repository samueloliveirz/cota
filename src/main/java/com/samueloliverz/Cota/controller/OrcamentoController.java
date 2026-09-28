package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.service.OrcamentoService;
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
    public Orcamento criar(@RequestBody Orcamento orcamento) {
        return service.salvar(orcamento);
    }

    @GetMapping
    public List<Orcamento> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Orcamento buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}/status")
    public Orcamento mudarStatus(@PathVariable Long id, @RequestParam StatusOrcamento status) {
        return service.mudarStatus(id, status);
    }

    @GetMapping("/cnpj/{cnpj}")
    public List<Orcamento> historicoCnpj(@PathVariable String cnpj) {
        return service.historicoCnpj(cnpj);
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