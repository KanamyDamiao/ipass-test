package com.ipass.task.service;

import com.ipass.subtask.domain.SubtaskModel;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.repository.SubtaskRepository;
import com.ipass.task.command.CreateTaskCommand;
import com.ipass.task.domain.TaskModel;
import com.ipass.task.domain.TaskStatus;
import com.ipass.task.exception.TaskCompletionWithPendingSubtasksException;
import com.ipass.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private SubtaskRepository subtaskRepository;

	@InjectMocks
	private TaskService taskService;

	@Test
	@DisplayName("Criar tarefa com sucesso")
	void shouldCreateTaskSuccessfully() {
		// given
		CreateTaskCommand cmd = CreateTaskCommand.builder()
												 .titulo("Tarefa 1")
												 .descricao("Descricao")
												 .usuarioId(UUID.randomUUID())
												 .build();

		UUID id = UUID.randomUUID();
		when(taskRepository.save(org.mockito.Mockito.any(TaskModel.class))).thenAnswer(invocation -> {
			TaskModel arg = invocation.getArgument(0);
			return new TaskModel(id,
								 arg.getTitulo(),
								 arg.getDescricao(),
								 arg.getStatus(),
								 arg.getDataCriacao(),
								 arg.getDataConclusao(),
								 arg.getUsuarioId());
		});

		// when
		TaskModel result = taskService.handle(cmd);

		// then
		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals(cmd.getTitulo(), result.getTitulo());
		assertEquals(cmd.getDescricao(), result.getDescricao());
		assertEquals(TaskStatus.PENDENTE, result.getStatus());
	}

	@Test
	@DisplayName("Atualizar status da tarefa para CONCLUIDA quando todas subtarefas estao concluidas")
	void shouldCompleteTaskWhenAllSubtasksAreCompleted() {
		// given
		UUID taskId = UUID.randomUUID();
		TaskModel task = new TaskModel(taskId,
									   "Tarefa 1",
									   "Descricao",
									   TaskStatus.EM_ANDAMENTO,
									   LocalDateTime.now(),
									   null,
									   UUID.randomUUID());

		when(taskRepository.findByIdOrThrowNotFound(taskId)).thenReturn(task);
		when(subtaskRepository.findByTarefaIdAndStatusNot(taskId,
														  SubtaskStatus.CONCLUIDA)).thenReturn(Collections.emptyList());

		lenient().when(taskRepository.save(org.mockito.Mockito.any(TaskModel.class)))
				 .thenAnswer(invocation -> invocation.getArgument(0));

		// when
		TaskModel result = taskService.updateStatus(taskId, TaskStatus.CONCLUIDA);

		// then
		assertNotNull(result);
		assertEquals(TaskStatus.CONCLUIDA, result.getStatus());
		assertNotNull(result.getDataConclusao());
	}

	@Test
	@DisplayName("Nao permitir concluir tarefa com subtarefas pendentes")
	void shouldNotCompleteTaskWhenThereArePendingSubtasks() {
		// given
		UUID taskId = UUID.randomUUID();
		TaskModel task = new TaskModel(taskId,
									   "Tarefa 1",
									   "Descricao",
									   TaskStatus.EM_ANDAMENTO,
									   LocalDateTime.now(),
									   null,
									   UUID.randomUUID());

		when(taskRepository.findByIdOrThrowNotFound(taskId)).thenReturn(task);

		SubtaskModel pendingSubtask = SubtaskModel.builder().titulo("Sub 1").descricao("Desc").tarefaId(taskId).build();

		when(subtaskRepository.findByTarefaIdAndStatusNot(taskId, SubtaskStatus.CONCLUIDA)).thenReturn(List.of(
				pendingSubtask));

		// when // then
		assertThrows(TaskCompletionWithPendingSubtasksException.class,
					 () -> taskService.updateStatus(taskId, TaskStatus.CONCLUIDA));
	}

	@Test
	@DisplayName("Atualizar status da tarefa para EM_ANDAMENTO sem definir dataConclusao")
	void shouldUpdateTaskToInProgressWithoutCompletionDate() {
		// given
		UUID taskId = UUID.randomUUID();
		TaskModel task = new TaskModel(taskId,
									   "Tarefa 1",
									   "Descricao",
									   TaskStatus.PENDENTE,
									   LocalDateTime.now(),
									   null,
									   UUID.randomUUID());

		when(taskRepository.findByIdOrThrowNotFound(taskId)).thenReturn(task);
		lenient().when(taskRepository.save(org.mockito.Mockito.any(TaskModel.class)))
				 .thenAnswer(invocation -> invocation.getArgument(0));

		// when
		TaskModel result = taskService.updateStatus(taskId, TaskStatus.EM_ANDAMENTO);

		// then
		assertNotNull(result);
		assertEquals(TaskStatus.EM_ANDAMENTO, result.getStatus());
		assertEquals(task.getDataCriacao(), result.getDataCriacao());
		assertEquals(task.getUsuarioId(), result.getUsuarioId());
		assertNull(result.getDataConclusao());
	}
}

