package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import com.samueloliverz.Cota.repository.OrcamentoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository repository;

    @Transactional
    public Orcamento salvar(Orcamento orcamento) {
        orcamento.getItens().forEach(item -> item.setOrcamento(orcamento));
        orcamento.setCnpj(limparCnpj(orcamento.getCnpj()));
        validarRegras(orcamento);
        return repository.save(orcamento);
    }

    public List<Orcamento> listarTodos() {
        return repository.findAll();
    }

    public Orcamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento " + id + " não encontrado"));
    }

    @Transactional
    public Orcamento mudarStatus(Long id, StatusOrcamento novoStatus) {
        Orcamento orcamento = buscarPorId(id);
        orcamento.setStatus(novoStatus);
        return orcamento;
    }

    public List<Orcamento> historicoCnpj(String cnpj) {
        return repository.findByCnpjOrderByDataCriacaoDesc(limparCnpj(cnpj));
    }

    public boolean clienteRecorrente(String cnpj) {
        return repository.existsByCnpjAndStatus(limparCnpj(cnpj), StatusOrcamento.FECHADO);
    }


    private void validarRegras(Orcamento orcamento) {
        if (orcamento.getItens().isEmpty()) {
            throw new IllegalArgumentException("O orçamento precisa ter pelo menos um item");
        }
        if (orcamento.getTipoRetirada() == TipoRetirada.SEDEX
                && (orcamento.getEnderecoEnvio() == null || orcamento.getEnderecoEnvio().isBlank())) {
            throw new IllegalArgumentException("Sedex precisa de endereço de envio");
        }
        if (orcamento.getFormaPagamento() == FormaPagamento.FATURADO
                && (orcamento.getDiasFaturamento() == null || orcamento.getDiasFaturamento() <= 0)) {
            throw new IllegalArgumentException("Faturado precisa dos dias de faturamento");
        }
    }


    @Transactional
    public void removerItem(Long orcamentoId, Long itemId) {
        Orcamento orcamento = buscarPorId(orcamentoId);

        if (orcamento.getStatus() == StatusOrcamento.FECHADO) {
            throw new IllegalStateException("Orçamento fechado não pode ser alterado");
        }

        boolean removeu = orcamento.getItens().removeIf(item -> item.getId().equals(itemId));
        if (!removeu) {
            throw new RuntimeException("Item " + itemId + " não pertence a esse orçamento");
        }
        if (orcamento.getItens().isEmpty()) {
            throw new IllegalArgumentException("O orçamento precisa ter pelo menos um item");
        }
    }

    private String limparCnpj(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }

}
