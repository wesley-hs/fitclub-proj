package com.pucminas.fitclub.service;

import com.pucminas.fitclub.repository.entity.Aluno;
import com.pucminas.fitclub.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Ativa o Mock
public class MatriculaServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    void deveVincularPlanoAoAluno_comSucesso() {
        // CENÁRIO
        String alunoId = "1";
        String planoId = "999";

        Aluno alunoFake = new Aluno();
        alunoFake.setId(alunoId);
        alunoFake.setNome("Wesley");

        // Quando o service procurar o aluno "1", retorne o nome do Aluno
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(alunoFake));

        // Quando o service salvar, retorne o próprio aluno (simulando sucesso)
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(i -> i.getArguments()[0]);

        // AÇÃO
        Aluno resultado = matriculaService.vincularPlano(alunoId, planoId);

        // VERIFICAÇÃO
        assertNotNull(resultado);
        assertEquals(planoId, resultado.getPlanoId()); // Garante que o ID do plano foi gravado no aluno
        verify(alunoRepository, times(1)).save(any()); // Garante que o método save foi chamado
    }

    @Test
    void deveLancarExcecaoQuandoPlanoNaoExistir() {
        // CENÁRIO
        String alunoId = "1";
        String planoId = "999";

        // Simulamos que o aluno existe, mas o plano não (Optional.empty)
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(new Aluno()));


        // AÇÃO & VERIFICAÇÃO
        assertThrows(RuntimeException.class, () -> {
            matriculaService.vincularPlano(alunoId, planoId);
        });
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontrado() {
        when(alunoRepository.findById("invalido")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            matriculaService.vincularPlano("invalido", "plano-1");
        });
    }
}
