package com.samueloliverz.Cota.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(
        int status,
        String mensagem,
        Map<String, String> campos,
        LocalDateTime dataHora
) {
}