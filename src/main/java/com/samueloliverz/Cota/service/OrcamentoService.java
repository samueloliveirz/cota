package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.enums.FormaPagamento;
import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.enums.StatusOrcamento;
import com.samueloliverz.Cota.enums.TipoOrcamento;
import com.samueloliverz.Cota.enums.TipoRetirada;
import com.samueloliverz.Cota.exception.RecursoNaoEncontradoException;
import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.OrcamentoRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository repository;
    private final UsuarioLogadoService usuarioLogado;

    @Transactional
    public Orcamento salvar(Orcamento orcamento) {
        orcamento.setCnpj(limparCnpj(orcamento.getCnpj()));
        orcamento.setSetor(usuarioLogado.get().getSetor());
        validarRegras(orcamento);
        return repository.save(orcamento);
    }

    @Transactional
    public Orcamento atualizar(Long id, Orcamento dados) {
        Orcamento orcamento = buscarPorId(id);
        verificarSePodeAlterar(orcamento);

        orcamento.setTipo(dados.getTipo());
        orcamento.setEmpresa(dados.getEmpresa());
        orcamento.setCnpj(limparCnpj(dados.getCnpj()));
        orcamento.setTipoRetirada(dados.getTipoRetirada());
        orcamento.setEnderecoEnvio(dados.getEnderecoEnvio());
        orcamento.setFormaPagamento(dados.getFormaPagamento());
        orcamento.setDiasFaturamento(dados.getDiasFaturamento());
        orcamento.setFrete(dados.getFrete());
        orcamento.setObservacao(dados.getObservacao());

        orcamento.getItens().clear();
        dados.getItens().forEach(orcamento::adicionarItem);

        validarRegras(orcamento);
        return orcamento;
    }

    public List<Orcamento> listarTodos() {
        if (usuarioLogado.isAdmin()) {
            return repository.findAll();
        }
        return repository.findBySetor(setorDoUsuario());
    }

    public Page<Orcamento> buscar(String empresa, StatusOrcamento status, Pageable pageable) {
        String termo = empresa == null ? "" : empresa.trim();

        if (usuarioLogado.isAdmin()) {
            if (status == null) {
                return repository.findByEmpresaContainingIgnoreCase(termo, pageable);
            }
            return repository.findByEmpresaContainingIgnoreCaseAndStatus(termo, status, pageable);
        }

        Setor setor = setorDoUsuario();
        if (status == null) {
            return repository.findByEmpresaContainingIgnoreCaseAndSetor(termo, setor, pageable);
        }
        return repository.findByEmpresaContainingIgnoreCaseAndStatusAndSetor(termo, status, setor, pageable);
    }

    public Orcamento buscarPorId(Long id) {
        return repository.findById(id)
                .filter(this::podeVer)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento " + id + " não encontrado"));
    }

    @Transactional
    public Orcamento mudarStatus(Long id, StatusOrcamento novoStatus) {
        Orcamento orcamento = buscarPorId(id);
        orcamento.setStatus(novoStatus);
        return orcamento;
    }

    public List<Orcamento> historicoCnpj(String cnpj) {
        if (usuarioLogado.isAdmin()) {
            return repository.findByCnpjOrderByDataCriacaoDesc(limparCnpj(cnpj));
        }
        return repository.findByCnpjAndSetorOrderByDataCriacaoDesc(limparCnpj(cnpj), setorDoUsuario());
    }

    public boolean clienteRecorrente(String cnpj) {
        return repository.existsByCnpjAndStatus(limparCnpj(cnpj), StatusOrcamento.FECHADO);
    }

    @Transactional
    public void removerItem(Long orcamentoId, Long itemId) {
        Orcamento orcamento = buscarPorId(orcamentoId);
        verificarSePodeAlterar(orcamento);

        boolean removeu = orcamento.getItens().removeIf(item -> item.getId().equals(itemId));
        if (!removeu) {
            throw new RecursoNaoEncontradoException("Item " + itemId + " não encontrado nesse orçamento");
        }
        if (orcamento.getItens().isEmpty()) {
            throw new IllegalArgumentException("O orçamento precisa ter pelo menos um item");
        }
    }

    private boolean podeVer(Orcamento orcamento) {
        Usuario usuario = usuarioLogado.get();
        return usuario.getRole() == com.samueloliverz.Cota.enums.Role.ADMIN
                || orcamento.getSetor() == usuario.getSetor();
    }

    private Setor setorDoUsuario() {
        return usuarioLogado.get().getSetor();
    }

    private void verificarSePodeAlterar(Orcamento orcamento) {
        if (orcamento.getStatus() == StatusOrcamento.FECHADO) {
            throw new IllegalStateException("Orçamento fechado não pode ser alterado");
        }
    }

    private void validarRegras(Orcamento orcamento) {
        if (orcamento.getTipo() != TipoOrcamento.PRESENCIAL
                && (orcamento.getCnpj() == null || orcamento.getCnpj().isBlank())) {
            throw new IllegalArgumentException("Informe o CNPJ");
        }
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

    private String limparCnpj(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }

    public Map<String, Long> contarPorStatus() {
        boolean admin = usuarioLogado.isAdmin();
        Setor setor = setorDoUsuario();

        Map<String, Long> contagem = new LinkedHashMap<>();
        contagem.put("TODOS", admin ? repository.count() : repository.countBySetor(setor));

        for (StatusOrcamento status : StatusOrcamento.values()) {
            long total = admin
                    ? repository.countByStatus(status)
                    : repository.countBySetorAndStatus(setor, status);
            contagem.put(status.name(), total);
        }
        return contagem;
    }
}