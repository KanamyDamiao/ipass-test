package com.ipass.subtask.service;

import com.ipass.subtask.command.CreateSubtaskCommand;
import com.ipass.subtask.domain.SubtaskModel;
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
public class SubtaskService {

	private final SubtaskRepository subtaskRepository;

	public SubtaskModel handle(CreateSubtaskCommand cmd) {
		SubtaskModel subtask = SubtaskModel.builder()
										   .titulo(cmd.getTitulo())
										   .descricao(cmd.getDescricao())
										   .tarefaId(cmd.getTarefaId())
										   .build();

		return subtaskRepository.save(subtask);
	}

	public List<SubtaskModel> findByTarefaId(UUID tarefaId) {
		return subtaskRepository.findByTarefaId(tarefaId);
	}

	public SubtaskModel updateStatus(UUID id, SubtaskStatus novoStatus) {
		SubtaskModel subtask = subtaskRepository.findByIdOrThrowNotFound(id);
		SubtaskModel atualizado = subtask.atualizarStatus(novoStatus);
		return subtaskRepository.save(atualizado);
	}
}

