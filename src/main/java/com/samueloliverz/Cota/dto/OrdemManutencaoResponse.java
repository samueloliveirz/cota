package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.OrdemManutencao;

import java.time.LocalDateTime;
import java.util.List;

public record OrdemManutencaoResponse(
        Long id,
        String empresa,
        String comprador,
        String telefone,
        String email,
        List<ItemManutencaoResponse> itens,
        String problemaRelatado,
        String observacao,
        String observacaoInterna,
        Setor setor,
        StatusOrcamento status,
        LocalDateTime dataCriacao
) {

    public static OrdemManutencaoResponse from(OrdemManutencao ordem) {
        return new OrdemManutencaoResponse(
                ordem.getId(),
                ordem.getEmpresa(),
                ordem.getComprador(),
                ordem.getTelefone(),
                ordem.getEmail(),
                ordem.getItens().stream().map(ItemManutencaoResponse::from).toList(),
                ordem.getProblemaRelatado(),
                ordem.getObservacao(),
                ordem.getObservacaoInterna(),
                ordem.getSetor(),
                ordem.getStatus(),
                ordem.getDataCriacao()
        );
    }
}