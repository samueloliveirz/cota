package com.samueloliverz.Cota.model;

import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.enums.Setor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}