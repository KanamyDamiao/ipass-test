package com.ipass.subtask.api;

import com.ipass.subtask.api.dto.request.CreateSubtaskRequest;
import com.ipass.subtask.api.dto.response.SubtaskResponse;
import com.ipass.subtask.api.mapper.SubtaskMapper;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.service.SubtaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class SubtaskController {

	private final SubtaskService service;

	public SubtaskController(SubtaskService service) {
		this.service = service;
	}

	@Operation(description = "Cria uma nova subtarefa para uma tarefa", method = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Subtarefa criada com sucesso."),
			@ApiResponse(responseCode = "400", description = "Subtarefa não pode ser criada.") })
	@PostMapping("/tarefas/{tarefaId}/subtarefas")
	public ResponseEntity<SubtaskResponse> createSubtask(@PathVariable UUID tarefaId,
														 @RequestBody @Valid CreateSubtaskRequest dto) {
		dto.setTarefaId(tarefaId);
		var command = SubtaskMapper.toCommand(dto);
		var created = service.handle(command);
		var response = SubtaskMapper.toResponse(created);
		return ResponseEntity.status(201).body(response);
	}

	@Operation(description = "Lista subtarefas de uma tarefa", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Subtarefas listadas com sucesso."),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.") })

	@GetMapping("/tarefas/{tarefaId}/subtarefas")
	public ResponseEntity<List<SubtaskResponse>> listSubtasks(@PathVariable UUID tarefaId) {
		var subtasks = service.findByTarefaId(tarefaId);
		var responses = subtasks.stream().map(SubtaskMapper::toResponse).toList();
		return ResponseEntity.ok(responses);
	}

	@Operation(description = "Lista subtarefas de uma tarefa filtradas por status", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Subtarefas listadas com sucesso."),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.") })
	@GetMapping("/tarefas/{tarefaId}/subtarefas/pesquisa")
	public ResponseEntity<List<SubtaskResponse>> listSubtasksByStatus(@PathVariable UUID tarefaId,
																   @RequestParam("status") SubtaskStatus status) {
		var subtasks = service.findByTarefaIdAndStatus(tarefaId, status);
		var responses = subtasks.stream().map(SubtaskMapper::toResponse).toList();
		return ResponseEntity.ok(responses);
	}

	@Operation(description = "Atualiza o status de uma subtarefa", method = "PATCH")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Status não pode ser atualizado.") })
	@PatchMapping("/subtarefas/{id}/status")
	public ResponseEntity<SubtaskResponse> updateSubtaskStatus(@PathVariable UUID id,
															   @RequestParam("status") SubtaskStatus status) {
		var updated = service.updateStatus(id, status);
		var response = SubtaskMapper.toResponse(updated);
		return ResponseEntity.ok(response);
	}
}
