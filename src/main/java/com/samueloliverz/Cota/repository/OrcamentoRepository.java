package com.samueloliverz.Cota.repository;

import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

        List<Orcamento> findByCnpjOrderByDataCriacaoDesc(String cnpj);

        boolean existsByCnpjAndStatus(String cnpj, StatusOrcamento status);

        Page<Orcamento> findByEmpresaContainingIgnoreCase(String empresa, Pageable pageable);

        Page<Orcamento> findByEmpresaContainingIgnoreCaseAndStatus(String empresa, StatusOrcamento status, Pageable pageable);
}