package com.ipass.subtask.domain;

import com.ipass.subtask.domain.SubtaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subtasks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskModel {

	@Id
	private UUID id;

	@Column(nullable = false)
	private String titulo;

	@Column
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SubtaskStatus status;

	@Column(name = "data_criacao", nullable = false)
	private LocalDateTime dataCriacao;

	@Column(name = "data_conclusao")
	private LocalDateTime dataConclusao;

	@Column(name = "tarefa_id", nullable = false)
	private UUID tarefaId;

	@Builder
	public SubtaskModel(String titulo, String descricao, UUID tarefaId) {
		this.id = UUID.randomUUID();
		this.titulo = titulo;
		this.descricao = descricao;
		this.tarefaId = tarefaId;
		this.status = SubtaskStatus.PENDENTE;
		this.dataCriacao = LocalDateTime.now();
	}
}

