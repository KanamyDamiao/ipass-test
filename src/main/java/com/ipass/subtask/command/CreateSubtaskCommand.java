package com.ipass.subtask.command;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public final class CreateSubtaskCommand {
	private String titulo;
	private String descricao;
	private UUID tarefaId;
}

