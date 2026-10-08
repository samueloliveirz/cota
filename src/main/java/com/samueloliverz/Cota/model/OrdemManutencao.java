package com.samueloliverz.Cota.model;

import com.samueloliverz.Cota.enums.Setor;
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

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Setor setor;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
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