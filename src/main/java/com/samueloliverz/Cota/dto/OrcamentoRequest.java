package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.TipoOrcamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import com.samueloliverz.Cota.model.Orcamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoRequest(
        @NotNull(message = "Informe o tipo do orçamento") TipoOrcamento tipo,
        @NotBlank(message = "Informe o nome da empresa")
        @Size(max = 255, message = "O nome da empresa pode ter no máximo 255 caracteres") String empresa,
        String cnpj,
        @Size(max = 255, message = "O nome do comprador pode ter no máximo 255 caracteres") String comprador,
        @Size(max = 30, message = "O telefone pode ter no máximo 30 caracteres") String telefone,
        @Email(message = "Email inválido")
        @Size(max = 255, message = "O email pode ter no máximo 255 caracteres") String email,
        @NotNull(message = "Informe o tipo de retirada") TipoRetirada tipoRetirada,
        @Size(max = 255, message = "O endereço pode ter no máximo 255 caracteres") String enderecoEnvio,
        @NotNull(message = "Informe a forma de pagamento") FormaPagamento formaPagamento,
        Integer diasFaturamento,
        @PositiveOrZero(message = "O frete não pode ser negativo") BigDecimal frete,
        @PositiveOrZero(message = "O desconto não pode ser negativo") BigDecimal desconto,
        @Size(max = 1000, message = "A observação pode ter no máximo 1000 caracteres") String observacao,
        @NotEmpty(message = "Adicione pelo menos um item") @Valid List<ItemRequest> itens
) {
    public Orcamento toEntity() {
        Orcamento orcamento = new Orcamento();
        orcamento.setTipo(tipo);
        orcamento.setEmpresa(empresa);
        orcamento.setCnpj(cnpj);
        orcamento.setComprador(comprador);
        orcamento.setTelefone(telefone);
        orcamento.setEmail(email);
        orcamento.setTipoRetirada(tipoRetirada);
        orcamento.setEnderecoEnvio(enderecoEnvio);
        orcamento.setFormaPagamento(formaPagamento);
        orcamento.setDiasFaturamento(diasFaturamento);
        orcamento.setFrete(frete);
        orcamento.setDesconto(desconto);
        orcamento.setObservacao(observacao);
        itens.forEach(item -> orcamento.adicionarItem(item.toEntity()));
        return orcamento;
    }
}