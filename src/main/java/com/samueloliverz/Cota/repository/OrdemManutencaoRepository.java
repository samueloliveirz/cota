package com.samueloliverz.Cota.repository;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.model.OrdemManutencao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemManutencaoRepository extends JpaRepository<OrdemManutencao, Long> {

    List<OrdemManutencao> findBySetor(Setor setor);

    Page<OrdemManutencao> findByClienteContainingIgnoreCase(String cliente, Pageable pageable);

    Page<OrdemManutencao> findByClienteContainingIgnoreCaseAndSetor(String cliente, Setor setor, Pageable pageable);
}