package com.ipass.task.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.config.BaseIntegrationTest;
import com.ipass.subtask.domain.SubtaskModel;
import com.ipass.subtask.repository.SubtaskRepository;
import com.ipass.task.api.dto.request.CreateTaskRequest;
import com.ipass.task.api.dto.request.UpdateTaskStatusRequest;
import com.ipass.task.domain.TaskModel;
import com.ipass.task.domain.TaskStatus;
import com.ipass.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends BaseIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private SubtaskRepository subtaskRepository;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		this.objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("Criar tarefa com sucesso")
	void shouldCreateTaskSuccessfully() throws Exception {
		// given
		CreateTaskRequest request = CreateTaskRequest.builder()
													 .titulo("Tarefa 1")
													 .descricao("Descricao")
													 .usuarioId(UUID.randomUUID())
													 .build();

		// when // then
		mockMvc.perform(post("/tarefas").contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.id").exists())
			   .andExpect(jsonPath("$.titulo", is("Tarefa 1")))
			   .andExpect(jsonPath("$.status", is(TaskStatus.PENDENTE.name())));
	}

	@Test
	@DisplayName("Listar tarefas filtradas por status")
	void shouldListTasksByStatus() throws Exception {
		// given
		UUID usuarioId = UUID.randomUUID();
		TaskModel pendente = new TaskModel(UUID.randomUUID(),
										   "Tarefa Pendente",
										   "Desc",
										   TaskStatus.PENDENTE,
										   LocalDateTime.now(),
										   null,
										   usuarioId);
		TaskModel emAndamento = new TaskModel(UUID.randomUUID(),
											  "Tarefa Andamento",
											  "Desc",
											  TaskStatus.EM_ANDAMENTO,
											  LocalDateTime.now(),
											  null,
											  usuarioId);
		taskRepository.save(pendente);
		taskRepository.save(emAndamento);

		// when // then
		mockMvc.perform(get("/tarefas").param("status", TaskStatus.PENDENTE.name()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$", hasSize(1)))
			   .andExpect(jsonPath("$[0].titulo", is("Tarefa Pendente")))
			   .andExpect(jsonPath("$[0].status", is(TaskStatus.PENDENTE.name())));
	}

	@Test
	@DisplayName("Listar tarefas filtradas por status e usuario")
	void shouldListTasksByStatusAndUser() throws Exception {
		// given
		UUID usuarioId1 = UUID.randomUUID();
		UUID usuarioId2 = UUID.randomUUID();
		TaskModel pendenteUser1 = new TaskModel(UUID.randomUUID(),
												"Tarefa Pendente U1",
												"Desc",
												TaskStatus.PENDENTE,
												LocalDateTime.now(),
												null,
												usuarioId1);
		TaskModel pendenteUser2 = new TaskModel(UUID.randomUUID(),
												"Tarefa Pendente U2",
												"Desc",
												TaskStatus.PENDENTE,
												LocalDateTime.now(),
												null,
												usuarioId2);
		taskRepository.save(pendenteUser1);
		taskRepository.save(pendenteUser2);

		// when // then
		mockMvc.perform(get("/tarefas/pesquisa").param("status", TaskStatus.PENDENTE.name())
												.param("usuarioId", usuarioId1.toString()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$", hasSize(1)))
			   .andExpect(jsonPath("$[0].titulo", is("Tarefa Pendente U1")))
			   .andExpect(jsonPath("$[0].status", is(TaskStatus.PENDENTE.name())))
			   .andExpect(jsonPath("$[0].usuarioId", is(usuarioId1.toString())));
	}

	@Test
	@DisplayName("Nao permitir concluir tarefa com subtarefas pendentes")
	void shouldNotAllowCompletingTaskWithPendingSubtasks() throws Exception {
		// given
		UUID usuarioId = UUID.randomUUID();
		TaskModel task = new TaskModel(UUID.randomUUID(),
									   "Tarefa 1",
									   "Desc",
									   TaskStatus.EM_ANDAMENTO,
									   LocalDateTime.now(),
									   null,
									   usuarioId);
		taskRepository.save(task);

		SubtaskModel sub = SubtaskModel.builder().titulo("Sub 1").descricao("Desc").tarefaId(task.getId()).build();
		subtaskRepository.save(sub);

		UpdateTaskStatusRequest request = UpdateTaskStatusRequest.builder().status(TaskStatus.CONCLUIDA).build();

		// when // then
		mockMvc.perform(patch("/tarefas/" + task.getId() + "/status").contentType(MediaType.APPLICATION_JSON)
																	 .content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Concluir tarefa sem subtarefas pendentes")
	void shouldCompleteTaskWithoutPendingSubtasks() throws Exception {
		// given
		UUID usuarioId = UUID.randomUUID();
		TaskModel task = new TaskModel(UUID.randomUUID(),
									   "Tarefa 1",
									   "Desc",
									   TaskStatus.EM_ANDAMENTO,
									   LocalDateTime.now(),
									   null,
									   usuarioId);
		taskRepository.save(task);

		UpdateTaskStatusRequest request = UpdateTaskStatusRequest.builder().status(TaskStatus.CONCLUIDA).build();

		// when // then
		mockMvc.perform(patch("/tarefas/" + task.getId() + "/status").contentType(MediaType.APPLICATION_JSON)
																	 .content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.status", is(TaskStatus.CONCLUIDA.name())));
	}
}

