package com.ipass.task.api.dto.response;

import com.ipass.task.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

	private UUID id;
	private String titulo;
	private String descricao;
	private TaskStatus status;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataConclusao;
	private UUID usuarioId;
}

