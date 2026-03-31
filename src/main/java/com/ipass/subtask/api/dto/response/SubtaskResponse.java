package com.ipass.subtask.api.dto.response;

import com.ipass.subtask.domain.SubtaskStatus;
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
public class SubtaskResponse {

	private UUID id;
	private String titulo;
	private String descricao;
	private SubtaskStatus status;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataConclusao;
	private UUID tarefaId;
}

