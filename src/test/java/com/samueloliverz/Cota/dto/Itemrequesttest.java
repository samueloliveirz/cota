package com.samueloliverz.Cota.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest {

    private ItemRequest comNcm(String ncm) {
        return new ItemRequest("Valvula hidraulica", "VAL-HID", ncm, 1, new BigDecimal("600.00"));
    }

    @Test
    void deveFormatarNcmDigitadoSoComNumeros() {
        assertThat(comNcm("84812090").toEntity().getNcm()).isEqualTo("8481.20.90");
    }

    @Test
    void deveManterNcmJaFormatado() {
        assertThat(comNcm("8481.20.90").toEntity().getNcm()).isEqualTo("8481.20.90");
    }

    @Test
    void deveGuardarNuloQuandoNcmEmBranco() {
        assertThat(comNcm(" ").toEntity().getNcm()).isNull();
    }
}