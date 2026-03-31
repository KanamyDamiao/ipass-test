package com.ipass.subtask.service;

import com.ipass.subtask.command.CreateSubtaskCommand;
import com.ipass.subtask.domain.SubtaskModel;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.repository.SubtaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubtaskServiceTest {

	@Mock
	private SubtaskRepository subtaskRepository;

	@InjectMocks
	private SubtaskService subtaskService;

	@Test
	@DisplayName("Criar subtarefa com sucesso")
	void shouldCreateSubtaskSuccessfully() {
		// given
		CreateSubtaskCommand cmd = CreateSubtaskCommand.builder()
													   .titulo("Sub 1")
													   .descricao("Desc")
													   .tarefaId(UUID.randomUUID())
													   .build();

		UUID id = UUID.randomUUID();
		when(subtaskRepository.save(org.mockito.Mockito.any(SubtaskModel.class))).thenAnswer(invocation -> {
			SubtaskModel arg = invocation.getArgument(0);
			return new SubtaskModel(id,
									arg.getTitulo(),
									arg.getDescricao(),
									arg.getStatus(),
									arg.getDataCriacao(),
									arg.getDataConclusao(),
									arg.getTarefaId());
		});

		// when
		SubtaskModel result = subtaskService.handle(cmd);

		// then
		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals(cmd.getTitulo(), result.getTitulo());
		assertEquals(cmd.getDescricao(), result.getDescricao());
		assertEquals(SubtaskStatus.PENDENTE, result.getStatus());
	}

	@Test
	@DisplayName("Atualizar status da subtarefa para CONCLUIDA preenchendo dataConclusao")
	void shouldCompleteSubtaskAndSetCompletionDate() {
		// given
		UUID id = UUID.randomUUID();
		SubtaskModel existing = new SubtaskModel(id,
												 "Sub 1",
												 "Desc",
												 SubtaskStatus.EM_ANDAMENTO,
												 LocalDateTime.now(),
												 null,
												 UUID.randomUUID());

		when(subtaskRepository.findByIdOrThrowNotFound(id)).thenReturn(existing);

		SubtaskModel completed = existing.atualizarStatus(SubtaskStatus.CONCLUIDA);
		lenient().when(subtaskRepository.save(org.mockito.Mockito.any(SubtaskModel.class)))
				 .thenAnswer(invocation -> invocation.getArgument(0));

		// when
		SubtaskModel result = subtaskService.updateStatus(id, SubtaskStatus.CONCLUIDA);

		// then
		assertNotNull(result);
		assertEquals(SubtaskStatus.CONCLUIDA, result.getStatus());
		assertNotNull(result.getDataConclusao());
	}

	@Test
	@DisplayName("Atualizar status da subtarefa para EM_ANDAMENTO sem preencher dataConclusao")
	void shouldUpdateSubtaskToInProgressWithoutCompletionDate() {
		// given
		UUID id = UUID.randomUUID();
		SubtaskModel existing = new SubtaskModel(id,
												 "Sub 1",
												 "Desc",
												 SubtaskStatus.PENDENTE,
												 LocalDateTime.now(),
												 null,
												 UUID.randomUUID());

		when(subtaskRepository.findByIdOrThrowNotFound(id)).thenReturn(existing);

		SubtaskModel inProgress = existing.atualizarStatus(SubtaskStatus.EM_ANDAMENTO);
		lenient().when(subtaskRepository.save(org.mockito.Mockito.any(SubtaskModel.class)))
				 .thenAnswer(invocation -> invocation.getArgument(0));

		// when
		SubtaskModel result = subtaskService.updateStatus(id, SubtaskStatus.EM_ANDAMENTO);

		// then
		assertNotNull(result);
		assertEquals(SubtaskStatus.EM_ANDAMENTO, result.getStatus());
		assertEquals(existing.getDataCriacao(), result.getDataCriacao());
		assertNull(result.getDataConclusao());
	}

	@Test
	@DisplayName("Listar subtarefas por tarefa com sucesso no service")
	void shouldListSubtasksByTaskSuccessfullyInService() {
		// given
		UUID tarefaId = UUID.randomUUID();
		SubtaskModel sub = new SubtaskModel(UUID.randomUUID(),
											"Sub 1",
											"Desc",
											SubtaskStatus.PENDENTE,
											LocalDateTime.now(),
											null,
											tarefaId);

		when(subtaskRepository.findByTarefaId(tarefaId)).thenReturn(List.of(sub));

		// when
		List<SubtaskModel> result = subtaskService.findByTarefaId(tarefaId);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(tarefaId, result.get(0).getTarefaId());
	}
}

