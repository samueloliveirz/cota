package com.samueloliverz.Cota.repository;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.model.Orcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

        List<Orcamento> findBySetor(Setor setor);

        List<Orcamento> findByCnpjOrderByDataCriacaoDesc(String cnpj);

        List<Orcamento> findByCnpjAndSetorOrderByDataCriacaoDesc(String cnpj, Setor setor);

        boolean existsByCnpjAndStatus(String cnpj, StatusOrcamento status);

        Page<Orcamento> findByEmpresaContainingIgnoreCase(String empresa, Pageable pageable);

        Page<Orcamento> findByEmpresaContainingIgnoreCaseAndStatus(String empresa, StatusOrcamento status, Pageable pageable);

        Page<Orcamento> findByEmpresaContainingIgnoreCaseAndSetor(String empresa, Setor setor, Pageable pageable);

        Page<Orcamento> findByEmpresaContainingIgnoreCaseAndStatusAndSetor(String empresa, StatusOrcamento status, Setor setor, Pageable pageable);

        long countBySetor(Setor setor);

        long countByStatus(StatusOrcamento status);

        long countBySetorAndStatus(Setor setor, StatusOrcamento status);
}