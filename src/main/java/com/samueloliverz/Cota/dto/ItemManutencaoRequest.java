package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.model.ItemManutencao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemManutencaoRequest(
        @NotNull(message = "Escolha o equipamento") ProdutoManutencao produto,
        @Size(max = 255, message = "Código muito longo") String codigo,
        @NotNull(message = "Informe a quantidade")
        @Positive(message = "A quantidade precisa ser maior que zero") Integer quantidade
) {

    public ItemManutencao toEntity() {
        ItemManutencao item = new ItemManutencao();
        item.setProduto(produto);
        item.setCodigo(codigo);
        item.setQuantidade(quantidade);
        return item;
    }
}