package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.model.ItemOrcamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank String produto,
        String codigo,
        @NotNull @Positive Integer quantidade,
        @NotNull @Positive BigDecimal precoUnitario
) {
    public ItemOrcamento toEntity() {
        ItemOrcamento item = new ItemOrcamento();
        item.setProduto(produto);
        item.setCodigo(codigo);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(precoUnitario);
        return item;
    }
}