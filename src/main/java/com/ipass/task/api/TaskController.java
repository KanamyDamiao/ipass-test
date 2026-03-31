package com.ipass.task.api;

import com.ipass.task.api.dto.request.CreateTaskRequest;
import com.ipass.task.api.dto.request.UpdateTaskStatusRequest;
import com.ipass.task.api.dto.response.TaskResponse;
import com.ipass.task.api.mapper.TaskMapper;
import com.ipass.task.domain.TaskStatus;
import com.ipass.task.service.TaskService;
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
@RequestMapping("/tarefas")
public class TaskController {

	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@Operation(description = "Cria uma nova tarefa", method = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso."),
			@ApiResponse(responseCode = "400", description = "Tarefa não pode ser criada.") })
	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest dto) {
		var command = TaskMapper.toCommand(dto);
		var created = service.handle(command);
		var response = TaskMapper.toResponse(created);
		return ResponseEntity.status(201).body(response);
	}

	@Operation(description = "Lista tarefas filtradas por status", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso."),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.") })

	@GetMapping
	public ResponseEntity<List<TaskResponse>> listTasks(@RequestParam(value = "status", required = false) TaskStatus status) {
		var tasks = service.findByStatus(status);
		var responses = tasks.stream().map(TaskMapper::toResponse).toList();
		return ResponseEntity.ok(responses);
	}

	@Operation(description = "Lista tarefas filtradas por status e usuário", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso."),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.") })
	@GetMapping("/pesquisa")
	public ResponseEntity<List<TaskResponse>> listTasksByStatusAndUser(@RequestParam("status") TaskStatus status,
																	   @RequestParam("usuarioId") UUID usuarioId) {
		var tasks = service.findByStatusAndUsuarioId(status, usuarioId);
		var responses = tasks.stream().map(TaskMapper::toResponse).toList();
		return ResponseEntity.ok(responses);
	}

	@Operation(description = "Atualiza o status de uma tarefa", method = "PATCH")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Status não pode ser atualizado.") })
	@PatchMapping("/{id}/status")
	public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID id,
														 @RequestBody @Valid UpdateTaskStatusRequest dto) {
		var updated = service.updateStatus(id, dto.getStatus());
		var response = TaskMapper.toResponse(updated);
		return ResponseEntity.ok(response);
	}
}

