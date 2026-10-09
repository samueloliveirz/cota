package com.samueloliverz.Cota.controller;

import com.samueloliverz.Cota.client.CnpjClient;
import com.samueloliverz.Cota.dto.CnpjResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CNPJ", description = "Consulta de dados da empresa na Receita")
@RestController
@RequestMapping("/cnpj")
@RequiredArgsConstructor
public class CnpjController {

    private final CnpjClient cnpjClient;

    @GetMapping("/{cnpj}")
    public ResponseEntity<CnpjResponse> consultar(@PathVariable String cnpj) {
        String numeros = cnpj.replaceAll("\\D", "");

        if (numeros.length() != 14) {
            return ResponseEntity.badRequest().build();
        }

        return cnpjClient.buscar(numeros)
                .map(dados -> new CnpjResponse(numeros, dados.razaoSocial(), dados.nomeFantasia()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}