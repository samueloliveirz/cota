package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.enums.*;
import com.samueloliverz.Cota.exception.RecursoNaoEncontradoException;
import com.samueloliverz.Cota.model.ItemOrcamento;
import com.samueloliverz.Cota.model.Orcamento;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.OrcamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository repository;

    @Mock
    private UsuarioLogadoService usuarioLogado;

    @InjectMocks
    private OrcamentoService service;

    private Usuario usuario(Setor setor, Role role) {
        Usuario usuario = new Usuario();
        usuario.setUsername("teste");
        usuario.setSetor(setor);
        usuario.setRole(role);
        return usuario;
    }

    private Orcamento orcamentoValido() {
        Orcamento orcamento = new Orcamento();
        orcamento.setTipo(TipoOrcamento.FORMALIZADO);
        orcamento.setEmpresa("Metalúrgica Teste");
        orcamento.setCnpj("12.345.678/0001-90");
        orcamento.setTipoRetirada(TipoRetirada.LOJA);
        orcamento.setFormaPagamento(FormaPagamento.A_VISTA);

        ItemOrcamento item = new ItemOrcamento();
        item.setProduto("Cilindro");
        item.setQuantidade(1);
        item.setPrecoUnitario(new BigDecimal("100.00"));
        orcamento.adicionarItem(item);

        return orcamento;
    }

    @Test
    void deveSalvarComOSetorDoUsuarioLogado() {
        Orcamento orcamento = orcamentoValido();
        when(usuarioLogado.get()).thenReturn(usuario(Setor.PNEUMATICA, Role.USER));
        when(repository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));

        Orcamento salvo = service.salvar(orcamento);

        assertThat(salvo.getSetor()).isEqualTo(Setor.PNEUMATICA);
        verify(repository).save(orcamento);
    }

    @Test
    void deveTirarAPontuacaoDoCnpjAoSalvar() {
        Orcamento orcamento = orcamentoValido();
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));
        when(repository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));

        Orcamento salvo = service.salvar(orcamento);

        assertThat(salvo.getCnpj()).isEqualTo("12345678000190");
    }

    @Test
    void naoDeveSalvarFormalizadoSemCnpj() {
        Orcamento orcamento = orcamentoValido();
        orcamento.setCnpj(null);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));

        assertThatThrownBy(() -> service.salvar(orcamento))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Informe o CNPJ");

        verify(repository, never()).save(any());
    }

    @Test
    void deveSalvarPresencialSemCnpj() {
        Orcamento orcamento = orcamentoValido();
        orcamento.setTipo(TipoOrcamento.PRESENCIAL);
        orcamento.setCnpj(null);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));
        when(repository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));

        Orcamento salvo = service.salvar(orcamento);

        assertThat(salvo.getCnpj()).isNull();
    }

    @Test
    void naoDeveSalvarSedexSemEndereco() {
        Orcamento orcamento = orcamentoValido();
        orcamento.setTipoRetirada(TipoRetirada.SEDEX);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));

        assertThatThrownBy(() -> service.salvar(orcamento))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sedex precisa de endereço de envio");
    }

    @Test
    void vendedorNaoVeOrcamentoDeOutroSetor() {
        Orcamento daHidraulica = orcamentoValido();
        daHidraulica.setSetor(Setor.HIDRAULICA);
        when(repository.findById(1L)).thenReturn(Optional.of(daHidraulica));
        when(usuarioLogado.get()).thenReturn(usuario(Setor.PNEUMATICA, Role.USER));

        assertThatThrownBy(() -> service.buscarPorId(1L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void adminVeOrcamentoDeQualquerSetor() {
        Orcamento daHidraulica = orcamentoValido();
        daHidraulica.setSetor(Setor.HIDRAULICA);
        when(repository.findById(1L)).thenReturn(Optional.of(daHidraulica));
        when(usuarioLogado.get()).thenReturn(usuario(Setor.PNEUMATICA, Role.ADMIN));

        Orcamento encontrado = service.buscarPorId(1L);

        assertThat(encontrado).isSameAs(daHidraulica);
    }

    @Test
    void naoDeveAlterarOrcamentoFechado() {
        Orcamento fechado = orcamentoValido();
        fechado.setSetor(Setor.HIDRAULICA);
        fechado.setStatus(StatusOrcamento.FECHADO);
        when(repository.findById(1L)).thenReturn(Optional.of(fechado));
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));

        assertThatThrownBy(() -> service.atualizar(1L, orcamentoValido()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Orçamento fechado não pode ser alterado");
    }
}