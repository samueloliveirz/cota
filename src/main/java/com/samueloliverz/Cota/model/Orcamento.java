package com.samueloliverz.Cota.model;

import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orcamento {

    private static final int DIAS_VALIDADE = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String empresa;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRetirada tipoRetirada;

    private String enderecoEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;

    private Integer diasFaturamento;

    @Column(precision = 12, scale = 2)
    private BigDecimal frete;

    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrcamento> itens = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = StatusOrcamento.ENVIADO;
        }
    }

    public void adicionarItem(ItemOrcamento item) {
        item.setOrcamento(this);
        itens.add(item);
    }

    public BigDecimal calcularSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemOrcamento item : itens) {
            subtotal = subtotal.add(item.calcularTotal());
        }
        return subtotal;
    }

    public BigDecimal calcularTotal() {
        BigDecimal valorFrete = frete != null ? frete : BigDecimal.ZERO;
        return calcularSubtotal().add(valorFrete);
    }

    public LocalDate getDataValidade() {
        return dataCriacao.toLocalDate().plusDays(DIAS_VALIDADE);
    }
}