package com.ipass.task.api.mapper;

import com.ipass.task.api.dto.request.CreateTaskRequest;
import com.ipass.task.api.dto.response.TaskResponse;
import com.ipass.task.command.CreateTaskCommand;
import com.ipass.task.domain.TaskModel;

public final class TaskMapper {

	private TaskMapper() {}

	public static CreateTaskCommand toCommand(CreateTaskRequest dto) {
		return CreateTaskCommand.builder()
				.titulo(dto.getTitulo())
				.descricao(dto.getDescricao())
				.usuarioId(dto.getUsuarioId())
				.build();
	}

	public static TaskResponse toResponse(TaskModel model) {
		return TaskResponse.builder()
				.id(model.getId())
				.titulo(model.getTitulo())
				.descricao(model.getDescricao())
				.status(model.getStatus())
				.dataCriacao(model.getDataCriacao())
				.dataConclusao(model.getDataConclusao())
				.usuarioId(model.getUsuarioId())
				.build();
	}
}

