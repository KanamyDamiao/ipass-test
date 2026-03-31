package com.ipass.task.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskModel {

	@Id
	private UUID id;

	@Column(nullable = false)
	private String titulo;

	@Column
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskStatus status;

	@Column(name = "data_criacao", nullable = false)
	private LocalDateTime dataCriacao;

	@Column(name = "data_conclusao")
	private LocalDateTime dataConclusao;

	@Column(name = "usuario_id", nullable = false)
	private UUID usuarioId;

	@Builder
	public TaskModel(String titulo, String descricao, UUID usuarioId) {
		this.id = UUID.randomUUID();
		this.titulo = titulo;
		this.descricao = descricao;
		this.usuarioId = usuarioId;
		this.status = TaskStatus.PENDENTE;
		this.dataCriacao = LocalDateTime.now();
	}

	public TaskModel atualizarStatus(TaskStatus novoStatus) {
		return new TaskModel(
				this.id,
				this.titulo,
				this.descricao,
				novoStatus,
				this.dataCriacao,
				novoStatus == TaskStatus.CONCLUIDA ? LocalDateTime.now() : null,
				this.usuarioId
		);
	}
}
