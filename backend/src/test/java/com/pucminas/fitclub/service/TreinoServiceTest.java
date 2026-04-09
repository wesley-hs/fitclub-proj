package com.pucminas.fitclub.service;

import com.pucminas.fitclub.repository.entity.Treino;
import com.pucminas.fitclub.repository.TreinoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreinoServiceTest {

    @Mock
    private TreinoRepository treinoRepository;

    @InjectMocks
    private TreinoService treinoService;

    @Test
    void deveCriarTreinoComSucesso() {
        // CENÁRIO
        Treino treinoInput = new Treino();
        treinoInput.setAlunoId("aluno-001");
        treinoInput.setNomeDoTreino("Treino A - Hipertrofia");
        treinoInput.setExercicios(List.of("Supino", "Crucifixo", "Tríceps Pulley"));


        Treino treinoSalvo = new Treino();
        treinoSalvo.setId("mongo-id-123");
        treinoSalvo.setAlunoId("aluno-001");
        treinoSalvo.setNomeDoTreino("Treino A - Hipertrofia");

        when(treinoRepository.save(any(Treino.class))).thenReturn(treinoSalvo);

        // AÇÃO
        Treino resultado = treinoService.salvarTreino(treinoInput);

        // VERIFICAÇÃO
        assertNotNull(resultado);
        assertEquals("mongo-id-123", resultado.getId());
        assertEquals("aluno-001", resultado.getAlunoId());

        // Verifica se o método save do repositório foi chamado exatamente uma vez
        verify(treinoRepository, times(1)).save(any(Treino.class));
    }

    @Test
    void deveListarTreinosPorAluno() {
        // CENÁRIO
        String alunoId = "aluno-001";
        when(treinoRepository.findAll()).thenReturn(List.of(new Treino(), new Treino()));

        // AÇÃO

        List<Treino> lista = treinoService.listarTreinoPorAluno(alunoId);

        // VERIFICAÇÃO
        assertFalse(lista.isEmpty());
        assertEquals(2, lista.size());
    }

    @Test
    void deveDeletarTreinoComSucesso() {
        // CENÁRIO
        String treinoId = "treino-123";
        doNothing().when(treinoRepository).deleteById(treinoId);

        // AÇÃO
        treinoService.deletar(treinoId); // Ajuste para o nome do seu método de deletar

        // VERIFICAÇÃO
        verify(treinoRepository, times(1)).deleteById(treinoId);
    }

    @Test
    void deveAtualizarTreinoComSucesso() {
        // Cenário
        String id = "123";
        Treino treinoExistente = new Treino();
        treinoExistente.setId(id);
        treinoExistente.setNomeDoTreino("Antigo");

        Treino treinoAtualizado = new Treino();
        treinoAtualizado.setNomeDoTreino("Novo Nome");

        when(treinoRepository.findById(id)).thenReturn(Optional.of(treinoExistente));
        when(treinoRepository.save(any(Treino.class))).thenReturn(treinoExistente);

        // Ação
        Treino resultado = treinoService.atulizarTreino(id, treinoAtualizado);

        // Verificação
        assertEquals("Novo Nome", resultado.getNomeDoTreino());
        verify(treinoRepository, times(1)).save(any());
    }

}
