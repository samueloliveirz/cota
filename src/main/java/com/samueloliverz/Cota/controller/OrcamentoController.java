package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.dto.OrcamentoRequest;
import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.service.OrcamentoService;
import com.samueloliverz.Cota.service.PdfService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Orçamentos", description = "Criação, edição, status e PDF de orçamentos")
@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor

public class OrcamentoController {

    private final OrcamentoService service;
    private final PdfService pdfService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrcamentoResponse criar(@Valid @RequestBody OrcamentoRequest request) {
        return OrcamentoResponse.from(service.salvar(request.toEntity()));
    }

    @PutMapping("/{id}")
    public OrcamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest request) {
        return OrcamentoResponse.from(service.atualizar(id, request.toEntity()));
    }

    @GetMapping
    public List<OrcamentoResponse> listarTodos() {
        return service.listarTodos().stream().map(OrcamentoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public OrcamentoResponse buscarPorId(@PathVariable Long id) {
        return OrcamentoResponse.from(service.buscarPorId(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
        OrcamentoResponse orcamento = OrcamentoResponse.from(service.buscarPorId(id));
        byte[] pdf = pdfService.gerarOrcamento(orcamento);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=orcamento-" + id + ".pdf")
                .body(pdf);
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