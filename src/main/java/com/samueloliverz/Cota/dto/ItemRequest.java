package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.model.ItemOrcamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank(message = "Informe o nome do produto") String produto,
        String codigo,
        @Pattern(regexp = "\\d{4}\\.?\\d{2}\\.?\\d{2}", message = "NCM inválido, use 8 dígitos, ex.: 8481.20.90") String ncm,
        @NotNull(message = "Informe a quantidade")
        @Positive(message = "A quantidade precisa ser maior que zero") Integer quantidade,
        @NotNull(message = "Informe o preço unitário")
        @Positive(message = "O preço precisa ser maior que zero") BigDecimal precoUnitario
) {
    public ItemOrcamento toEntity() {
        ItemOrcamento item = new ItemOrcamento();
        item.setProduto(produto);
        item.setCodigo(codigo);
        item.setNcm(formatarNcm(ncm));
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(precoUnitario);
        return item;
    }

    private static String formatarNcm(String ncm) {
        if (ncm == null || ncm.isBlank()) {
            return null;
        }
        String digitos = ncm.replaceAll("\\D", "");
        return digitos.substring(0, 4) + "." + digitos.substring(4, 6) + "." + digitos.substring(6);
    }
}