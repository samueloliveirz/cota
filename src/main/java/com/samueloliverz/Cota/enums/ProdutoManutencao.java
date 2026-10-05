package com.samueloliverz.Cota.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProdutoManutencao {
    CILINDRO("Cilindro"),
    BOMBA("Bomba"),
    VALVULA("Válvula"),
    UNIDADE_HIDRAULICA("Unidade hidráulica");

    private final String nome;
}