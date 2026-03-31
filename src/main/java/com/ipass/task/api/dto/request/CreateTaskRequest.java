package com.ipass.task.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

	@NotEmpty(message = "{CreateTaskRequest.titulo.NotEmpty}")
	private String titulo;

	private String descricao;

	@NotNull(message = "{CreateTaskRequest.usuarioId.NotNull}")
	private UUID usuarioId;
}

