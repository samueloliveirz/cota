package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.model.ItemOrcamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank(message = "Informe o nome do produto") String produto,
        String codigo,
        @NotNull(message = "Informe a quantidade")
        @Positive(message = "A quantidade precisa ser maior que zero") Integer quantidade,
        @NotNull(message = "Informe o preço unitário")
        @Positive(message = "O preço precisa ser maior que zero") BigDecimal precoUnitario
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