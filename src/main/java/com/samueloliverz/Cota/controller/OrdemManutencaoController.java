package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrdemManutencaoRequest;
import com.samueloliverz.Cota.dto.OrdemManutencaoResponse;
import com.samueloliverz.Cota.service.OrdemManutencaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manutenção", description = "Ordens de manutenção")
@RestController
@RequestMapping("/manutencoes")
@RequiredArgsConstructor
public class OrdemManutencaoController {

    private final OrdemManutencaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemManutencaoResponse criar(@Valid @RequestBody OrdemManutencaoRequest request) {
        return OrdemManutencaoResponse.from(service.salvar(request.toEntity()));
    }

    @PutMapping("/{id}")
    public OrdemManutencaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody OrdemManutencaoRequest request) {
        return OrdemManutencaoResponse.from(service.atualizar(id, request.toEntity()));
    }

    @GetMapping
    public List<OrdemManutencaoResponse> listarTodas() {
        return service.listarTodas().stream().map(OrdemManutencaoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public OrdemManutencaoResponse buscarPorId(@PathVariable Long id) {
        return OrdemManutencaoResponse.from(service.buscarPorId(id));
    }
}