package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.enums.TipoOrcamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import com.samueloliverz.Cota.model.Orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrcamentoResponse(
        Long id,
        TipoOrcamento tipo,
        Setor setor,
        String empresa,
        String cnpj,
        String comprador,
        String telefone,
        String email,
        TipoRetirada tipoRetirada,
        String enderecoEnvio,
        FormaPagamento formaPagamento,
        Integer diasFaturamento,
        String observacao,
        StatusOrcamento status,
        LocalDateTime dataCriacao,
        LocalDate dataValidade,
        List<ItemResponse> itens,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal frete,
        BigDecimal total
) {
    public static OrcamentoResponse from(Orcamento orcamento) {
        return new OrcamentoResponse(
                orcamento.getId(),
                orcamento.getTipo(),
                orcamento.getSetor(),
                orcamento.getEmpresa(),
                orcamento.getCnpj(),
                orcamento.getComprador(),
                orcamento.getTelefone(),
                orcamento.getEmail(),
                orcamento.getTipoRetirada(),
                orcamento.getEnderecoEnvio(),
                orcamento.getFormaPagamento(),
                orcamento.getDiasFaturamento(),
                orcamento.getObservacao(),
                orcamento.getStatus(),
                orcamento.getDataCriacao(),
                orcamento.getDataValidade(),
                orcamento.getItens().stream().map(ItemResponse::from).toList(),
                orcamento.calcularSubtotal(),
                orcamento.getDesconto(),
                orcamento.getFrete(),
                orcamento.calcularTotal()
        );
    }
}