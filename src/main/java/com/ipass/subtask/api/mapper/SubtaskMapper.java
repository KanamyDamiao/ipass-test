package com.ipass.subtask.api.mapper;

import com.ipass.subtask.api.dto.request.CreateSubtaskRequest;
import com.ipass.subtask.api.dto.response.SubtaskResponse;
import com.ipass.subtask.command.CreateSubtaskCommand;
import com.ipass.subtask.domain.SubtaskModel;

public final class SubtaskMapper {

	private SubtaskMapper() {}

	public static CreateSubtaskCommand toCommand(CreateSubtaskRequest dto) {
		return CreateSubtaskCommand.builder()
				.titulo(dto.getTitulo())
				.descricao(dto.getDescricao())
				.tarefaId(dto.getTarefaId())
				.build();
	}

	public static SubtaskResponse toResponse(SubtaskModel model) {
		return SubtaskResponse.builder()
				.id(model.getId())
				.titulo(model.getTitulo())
				.descricao(model.getDescricao())
				.status(model.getStatus())
				.dataCriacao(model.getDataCriacao())
				.dataConclusao(model.getDataConclusao())
				.tarefaId(model.getTarefaId())
				.build();
	}
}

