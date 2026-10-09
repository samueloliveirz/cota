package com.samueloliverz.Cota.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjWsResposta(
        @JsonProperty("razao_social") String razaoSocial,
        Estabelecimento estabelecimento
) {

    public String nomeFantasia() {
        return estabelecimento != null ? estabelecimento.nomeFantasia() : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Estabelecimento(
            @JsonProperty("nome_fantasia") String nomeFantasia
    ) {
    }
}