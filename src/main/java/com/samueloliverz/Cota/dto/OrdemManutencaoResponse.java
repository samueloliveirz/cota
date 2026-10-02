package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.model.OrdemManutencao;

import java.time.LocalDateTime;

public record OrdemManutencaoResponse(
        Long id,
        String cliente,
        String telefone,
        ProdutoManutencao produto,
        String codigoProduto,
        String problemaRelatado,
        String observacao,
        LocalDateTime dataCriacao
) {

    public static OrdemManutencaoResponse from(OrdemManutencao ordem) {
        return new OrdemManutencaoResponse(
                ordem.getId(),
                ordem.getCliente(),
                ordem.getTelefone(),
                ordem.getProduto(),
                ordem.getCodigoProduto(),
                ordem.getProblemaRelatado(),
                ordem.getObservacao(),
                ordem.getDataCriacao()
        );
    }
}