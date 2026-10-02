package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.model.OrdemManutencao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemManutencaoRequest(
        @NotBlank(message = "Informe o nome do cliente") String cliente,
        @NotBlank(message = "Informe o telefone") String telefone,
        @NotNull(message = "Informe o produto") ProdutoManutencao produto,
        String codigoProduto,
        @NotBlank(message = "Informe o problema relatado") String problemaRelatado,
        String observacao
) {

    public OrdemManutencao toEntity() {
        OrdemManutencao ordemManutencao = new OrdemManutencao();
        ordemManutencao.setCliente(cliente);
        ordemManutencao.setTelefone(telefone);
        ordemManutencao.setProduto(produto);
        ordemManutencao.setCodigoProduto(codigoProduto);
        ordemManutencao.setProblemaRelatado(problemaRelatado);
        ordemManutencao.setObservacao(observacao);
        return ordemManutencao;
    }
}