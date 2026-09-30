package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import com.samueloliverz.Cota.model.Orcamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoRequest(
        @NotBlank(message = "Informe o nome da empresa") String empresa,
        @NotBlank(message = "Informe o CNPJ") String cnpj,
        @NotNull(message = "Informe o tipo de retirada") TipoRetirada tipoRetirada,
        String enderecoEnvio,
        @NotNull(message = "Informe a forma de pagamento") FormaPagamento formaPagamento,
        Integer diasFaturamento,
        @PositiveOrZero(message = "O frete não pode ser negativo") BigDecimal frete,
        String observacao,
        @NotEmpty(message = "Adicione pelo menos um item") @Valid List<ItemRequest> itens
) {
    public Orcamento toEntity() {
        Orcamento orcamento = new Orcamento();
        orcamento.setEmpresa(empresa);
        orcamento.setCnpj(cnpj);
        orcamento.setTipoRetirada(tipoRetirada);
        orcamento.setEnderecoEnvio(enderecoEnvio);
        orcamento.setFormaPagamento(formaPagamento);
        orcamento.setDiasFaturamento(diasFaturamento);
        orcamento.setFrete(frete);
        orcamento.setObservacao(observacao);
        itens.forEach(item -> orcamento.adicionarItem(item.toEntity()));
        return orcamento;
    }
}