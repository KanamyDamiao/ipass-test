package com.ipass.task.service;

import com.ipass.task.command.CreateTaskCommand;
import com.ipass.task.domain.TaskModel;
import com.ipass.task.domain.TaskStatus;
import com.ipass.task.exception.TaskCompletionWithPendingSubtasksException;
import com.ipass.task.repository.TaskRepository;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.repository.SubtaskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TaskService {

	private final TaskRepository taskRepository;
	private final SubtaskRepository subtaskRepository;

	public TaskModel handle(CreateTaskCommand cmd) {
		TaskModel task = TaskModel.builder()
				.titulo(cmd.getTitulo())
				.descricao(cmd.getDescricao())
				.usuarioId(cmd.getUsuarioId())
				.build();

		return taskRepository.save(task);
	}

	public TaskModel findById(UUID id) {
		return taskRepository.findByIdOrThrowNotFound(id);
	}

	public List<TaskModel> findByStatus(TaskStatus status) {
		if (status == null) {
			return taskRepository.findAll();
		}
		return taskRepository.findByStatus(status);
	}

	public TaskModel updateStatus(UUID id, TaskStatus novoStatus) {
		TaskModel task = taskRepository.findByIdOrThrowNotFound(id);

		if (novoStatus == TaskStatus.CONCLUIDA) {
			var pendentes = subtaskRepository.findByTarefaIdAndStatusNot(id, SubtaskStatus.CONCLUIDA);
			if (!pendentes.isEmpty()) {
				throw new TaskCompletionWithPendingSubtasksException();
			}
		}

		TaskModel atualizado = task.atualizarStatus(novoStatus);
		return taskRepository.save(atualizado);
	}
}

