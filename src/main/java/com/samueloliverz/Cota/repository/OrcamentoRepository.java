package com.samueloliverz.Cota.repository;

import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

        List<Orcamento> findByCnpjOrderByDataCriacaoDesc(String cnpj);

        boolean existsByCnpjAndStatus(String cnpj, StatusOrcamento status);


}
