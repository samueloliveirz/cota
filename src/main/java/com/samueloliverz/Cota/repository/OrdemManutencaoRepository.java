package com.samueloliverz.Cota.repository;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.OrdemManutencao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemManutencaoRepository extends JpaRepository<OrdemManutencao, Long> {

    List<OrdemManutencao> findBySetor(Setor setor);

    Page<OrdemManutencao> findByEmpresaContainingIgnoreCase(String empresa, Pageable pageable);

    Page<OrdemManutencao> findByEmpresaContainingIgnoreCaseAndStatus(String empresa, StatusOrcamento status, Pageable pageable);

    Page<OrdemManutencao> findByEmpresaContainingIgnoreCaseAndSetor(String empresa, Setor setor, Pageable pageable);

    Page<OrdemManutencao> findByEmpresaContainingIgnoreCaseAndStatusAndSetor(String empresa, StatusOrcamento status, Setor setor, Pageable pageable);

    long countBySetor(Setor setor);

    long countByStatus(StatusOrcamento status);

    long countBySetorAndStatus(Setor setor, StatusOrcamento status);
}