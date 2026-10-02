package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.exception.RecursoNaoEncontradoException;
import com.samueloliverz.Cota.model.OrdemManutencao;
import com.samueloliverz.Cota.repository.OrdemManutencaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemManutencaoService {

    private final OrdemManutencaoRepository repository;

    @Transactional
    public OrdemManutencao salvar(OrdemManutencao ordem) {
        return repository.save(ordem);
    }

    @Transactional
    public OrdemManutencao atualizar(Long id, OrdemManutencao dados) {
        OrdemManutencao ordem = buscarPorId(id);

        ordem.setCliente(dados.getCliente());
        ordem.setTelefone(dados.getTelefone());
        ordem.setProduto(dados.getProduto());
        ordem.setCodigoProduto(dados.getCodigoProduto());
        ordem.setProblemaRelatado(dados.getProblemaRelatado());
        ordem.setObservacao(dados.getObservacao());

        return ordem;
    }

    public List<OrdemManutencao> listarTodas() {
        return repository.findAll();
    }

    public OrdemManutencao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de manutenção " + id + " não encontrada"));
    }
}