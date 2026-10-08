package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.exception.RecursoNaoEncontradoException;
import com.samueloliverz.Cota.model.OrdemManutencao;
import com.samueloliverz.Cota.repository.OrdemManutencaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemManutencaoService {

    private final OrdemManutencaoRepository repository;
    private final UsuarioLogadoService usuarioLogado;

    @Transactional
    public OrdemManutencao salvar(OrdemManutencao ordem) {
        ordem.setSetor(setorDoUsuario());
        return repository.save(ordem);
    }

    @Transactional
    public OrdemManutencao atualizar(Long id, OrdemManutencao dados) {
        OrdemManutencao ordem = buscarPorId(id);

        ordem.setEmpresa(dados.getEmpresa());
        ordem.setComprador(dados.getComprador());
        ordem.setTelefone(dados.getTelefone());
        ordem.setEmail(dados.getEmail());
        ordem.substituirItens(new ArrayList<>(dados.getItens()));
        ordem.setProblemaRelatado(dados.getProblemaRelatado());
        ordem.setObservacao(dados.getObservacao());

        return ordem;
    }

    public List<OrdemManutencao> listarTodas() {
        if (usuarioLogado.isAdmin()) {
            return repository.findAll();
        }
        return repository.findBySetor(setorDoUsuario());
    }

    public Page<OrdemManutencao> buscar(String empresa, Pageable pageable) {
        String termo = empresa == null ? "" : empresa.trim();

        if (usuarioLogado.isAdmin()) {
            return repository.findByEmpresaContainingIgnoreCase(termo, pageable);
        }
        return repository.findByEmpresaContainingIgnoreCaseAndSetor(termo, setorDoUsuario(), pageable);
    }

    public OrdemManutencao buscarPorId(Long id) {
        return repository.findById(id)
                .filter(this::podeVer)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de manutenção " + id + " não encontrada"));
    }

    private boolean podeVer(OrdemManutencao ordem) {
        return usuarioLogado.isAdmin() || ordem.getSetor() == setorDoUsuario();
    }

    private Setor setorDoUsuario() {
        return usuarioLogado.get().getSetor();
    }
}