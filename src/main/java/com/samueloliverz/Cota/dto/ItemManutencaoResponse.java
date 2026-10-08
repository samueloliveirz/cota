package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.model.ItemManutencao;

public record ItemManutencaoResponse(
        Long id,
        ProdutoManutencao produto,
        String codigo,
        Integer quantidade
) {

    public static ItemManutencaoResponse from(ItemManutencao item) {
        return new ItemManutencaoResponse(item.getId(), item.getProduto(), item.getCodigo(), item.getQuantidade());
    }
}