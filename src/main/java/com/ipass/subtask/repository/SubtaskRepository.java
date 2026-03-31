package com.ipass.subtask.repository;

import com.ipass.subtask.domain.SubtaskModel;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.exception.SubtaskNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<SubtaskModel, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	default SubtaskModel findByIdOrThrowNotFound(UUID id) {
		return findById(id).orElseThrow(SubtaskNotFoundException::new);
	}

	List<SubtaskModel> findByTarefaId(UUID tarefaId);

	List<SubtaskModel> findByTarefaIdAndStatusNot(UUID tarefaId, SubtaskStatus status);
}

