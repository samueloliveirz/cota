package com.samueloliverz.Cota.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrcamentoTest {

    private ItemOrcamento item(int quantidade, String preco) {
        ItemOrcamento item = new ItemOrcamento();
        item.setProduto("Produto teste");
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(new BigDecimal(preco));
        return item;
    }

    @Test
    void deveSomarOsItensNoSubtotal() {
        Orcamento orcamento = new Orcamento();
        orcamento.adicionarItem(item(2, "850.00"));
        orcamento.adicionarItem(item(4, "35.90"));

        BigDecimal subtotal = orcamento.calcularSubtotal();

        assertThat(subtotal).isEqualByComparingTo("1843.60");
    }

    @Test
    void deveSomarOFreteNoTotal() {
        Orcamento orcamento = new Orcamento();
        orcamento.adicionarItem(item(2, "850.00"));
        orcamento.adicionarItem(item(4, "35.90"));
        orcamento.setFrete(new BigDecimal("45.50"));

        BigDecimal total = orcamento.calcularTotal();

        assertThat(total).isEqualByComparingTo("1889.10");
    }

    @Test
    void deveConsiderarFreteZeroQuandoNaoInformado() {
        Orcamento orcamento = new Orcamento();
        orcamento.adicionarItem(item(1, "100.00"));

        BigDecimal total = orcamento.calcularTotal();

        assertThat(total).isEqualByComparingTo("100.00");
    }

    @Test
    void deveCalcularValidadeVinteDiasDepoisDaCriacao() {
        Orcamento orcamento = new Orcamento();
        orcamento.setDataCriacao(LocalDateTime.of(2026, 10, 1, 10, 0));

        LocalDate validade = orcamento.getDataValidade();

        assertThat(validade).isEqualTo(LocalDate.of(2026, 10, 21));
    }
}