package com.samueloliverz.Cota.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusOrcamento {
    ENVIADO("Enviado"),
    AGUARDADO("Aguardado"),
    FECHADO("Fechado"),
    NAO_FECHADO("Não fechado");

    private final String nome;
}