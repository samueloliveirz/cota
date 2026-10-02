package com.samueloliverz.Cota.model;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrdemManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProdutoManutencao produto;

    private String codigoProduto;

    @Column(nullable = false, length = 1000)
    private String problemaRelatado;

    @Column(length = 1000)
    private String observacao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}