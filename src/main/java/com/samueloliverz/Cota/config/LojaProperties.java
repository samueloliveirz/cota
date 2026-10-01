package com.samueloliverz.Cota.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "loja")
public record LojaProperties(
        String nome,
        String endereco,
        String cnpj,
        String telefone,
        String pix
) {
}