package com.samueloliverz.Cota.service;

import com.samueloliverz.Cota.enums.ProdutoManutencao;
import com.samueloliverz.Cota.enums.Role;
import com.samueloliverz.Cota.enums.Setor;
import com.samueloliverz.Cota.exception.RecursoNaoEncontradoException;
import com.samueloliverz.Cota.model.OrdemManutencao;
import com.samueloliverz.Cota.model.Usuario;
import com.samueloliverz.Cota.repository.OrdemManutencaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemManutencaoServiceTest {

    @Mock
    private OrdemManutencaoRepository repository;

    @Mock
    private UsuarioLogadoService usuarioLogado;

    @InjectMocks
    private OrdemManutencaoService service;

    private final Pageable pageable = PageRequest.of(0, 10);

    private Usuario usuario(Setor setor, Role role) {
        Usuario usuario = new Usuario();
        usuario.setUsername("teste");
        usuario.setSetor(setor);
        usuario.setRole(role);
        return usuario;
    }

    private OrdemManutencao ordem(Setor setor) {
        OrdemManutencao ordem = new OrdemManutencao();
        ordem.setCliente("Prensas Diadema");
        ordem.setTelefone("(11) 98765-1200");
        ordem.setProduto(ProdutoManutencao.CILINDRO);
        ordem.setProblemaRelatado("Vazamento pela haste");
        ordem.setSetor(setor);
        return ordem;
    }

    @Test
    void deveSalvarComOSetorDoUsuarioLogado() {
        OrdemManutencao nova = ordem(null);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.PNEUMATICA, Role.USER));
        when(repository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));

        OrdemManutencao salva = service.salvar(nova);

        assertThat(salva.getSetor()).isEqualTo(Setor.PNEUMATICA);
    }

    @Test
    void usuarioComumDeveBuscarSoNoProprioSetor() {
        when(usuarioLogado.isAdmin()).thenReturn(false);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));
        when(repository.findByClienteContainingIgnoreCaseAndSetor("prensas", Setor.HIDRAULICA, pageable))
                .thenReturn(Page.empty());

        service.buscar("prensas", pageable);

        verify(repository).findByClienteContainingIgnoreCaseAndSetor("prensas", Setor.HIDRAULICA, pageable);
        verify(repository, never()).findByClienteContainingIgnoreCase(any(), any());
    }

    @Test
    void adminDeveBuscarEmTodosOsSetores() {
        when(usuarioLogado.isAdmin()).thenReturn(true);
        when(repository.findByClienteContainingIgnoreCase("", pageable)).thenReturn(Page.empty());

        service.buscar(null, pageable);

        verify(repository).findByClienteContainingIgnoreCase("", pageable);
        verify(repository, never()).findByClienteContainingIgnoreCaseAndSetor(any(), any(), any());
    }

    @Test
    void naoDeveAcharOrdemDeOutroSetor() {
        when(repository.findById(1L)).thenReturn(Optional.of(ordem(Setor.PNEUMATICA)));
        when(usuarioLogado.isAdmin()).thenReturn(false);
        when(usuarioLogado.get()).thenReturn(usuario(Setor.HIDRAULICA, Role.USER));

        assertThatThrownBy(() -> service.buscarPorId(1L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void adminDeveAcharOrdemDeQualquerSetor() {
        OrdemManutencao existente = ordem(Setor.PNEUMATICA);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioLogado.isAdmin()).thenReturn(true);

        OrdemManutencao encontrada = service.buscarPorId(1L);

        assertThat(encontrada).isSameAs(existente);
    }

    @Test
    void atualizarNaoDeveMudarOSetorDaOrdem() {
        OrdemManutencao existente = ordem(Setor.PNEUMATICA);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioLogado.isAdmin()).thenReturn(true);

        OrdemManutencao dados = ordem(Setor.HIDRAULICA);
        dados.setCliente("Outro cliente");

        OrdemManutencao atualizada = service.atualizar(1L, dados);

        assertThat(atualizada.getCliente()).isEqualTo("Outro cliente");
        assertThat(atualizada.getSetor()).isEqualTo(Setor.PNEUMATICA);
    }
}