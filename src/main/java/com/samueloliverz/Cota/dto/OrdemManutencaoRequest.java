package com.samueloliverz.Cota.dto;

import com.samueloliverz.Cota.model.OrdemManutencao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrdemManutencaoRequest(
        @NotBlank(message = "Informe a empresa")
        @Size(max = 255, message = "Nome da empresa muito longo") String empresa,
        @Size(max = 255, message = "Nome do comprador muito longo") String comprador,
        @NotBlank(message = "Informe o telefone")
        @Size(max = 255, message = "Telefone muito longo") String telefone,
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "E-mail muito longo") String email,
        @NotEmpty(message = "Adicione pelo menos um equipamento") @Valid List<ItemManutencaoRequest> itens,
        @NotBlank(message = "Informe o problema relatado")
        @Size(max = 1000, message = "O problema relatado pode ter no máximo 1000 caracteres") String problemaRelatado,
        @Size(max = 1000, message = "A observação pode ter no máximo 1000 caracteres") String observacao,
        @Size(max = 1000, message = "A informação interna pode ter no máximo 1000 caracteres") String observacaoInterna
) {

    public OrdemManutencao toEntity() {
        OrdemManutencao ordem = new OrdemManutencao();
        ordem.setEmpresa(empresa);
        ordem.setComprador(comprador);
        ordem.setTelefone(telefone);
        ordem.setEmail(email);
        itens.forEach(item -> ordem.adicionarItem(item.toEntity()));
        ordem.setProblemaRelatado(problemaRelatado);
        ordem.setObservacao(observacao);
        ordem.setObservacaoInterna(observacaoInterna);
        return ordem;
    }
}