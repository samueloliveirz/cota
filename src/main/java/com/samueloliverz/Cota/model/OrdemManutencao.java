package com.samueloliverz.Cota.model;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrdemManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String empresa;

    private String comprador;

    private String telefone;

    private String email;

    @OneToMany(mappedBy = "ordem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemManutencao> itens = new ArrayList<>();

    @Column(nullable = false, length = 1000)
    private String problemaRelatado;

    @Column(length = 1000)
    private String observacao;

    @Column(length = 1000)
    private String observacaoInterna;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = StatusOrcamento.ENVIADO;
        }
    }

    public void adicionarItem(ItemManutencao item) {
        item.setOrdem(this);
        itens.add(item);
    }

    public void substituirItens(List<ItemManutencao> novos) {
        itens.clear();
        novos.forEach(this::adicionarItem);
    }
}