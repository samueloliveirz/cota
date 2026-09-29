package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.model.ItemOrcamento;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String produto,
        String codigo,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal total
) {
    public static ItemResponse from(ItemOrcamento item) {
        return new ItemResponse(
                item.getId(),
                item.getProduto(),
                item.getCodigo(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.calcularTotal()
        );
    }
}