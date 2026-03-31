package com.ipass.task.command;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public final class CreateTaskCommand {
	private String titulo;
	private String descricao;
	private UUID usuarioId;
}

