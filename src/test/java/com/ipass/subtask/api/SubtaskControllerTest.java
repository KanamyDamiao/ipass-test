package com.ipass.subtask.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.config.BaseIntegrationTest;
import com.ipass.subtask.api.dto.request.CreateSubtaskRequest;
import com.ipass.subtask.domain.SubtaskModel;
import com.ipass.subtask.domain.SubtaskStatus;
import com.ipass.subtask.repository.SubtaskRepository;
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

class SubtaskControllerTest extends BaseIntegrationTest {

	@Autowired
	private WebApplicationContext context;

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
	@DisplayName("Criar subtarefa com sucesso")
	void shouldCreateSubtaskSuccessfully() throws Exception {
		// given
		UUID tarefaId = UUID.randomUUID();
		CreateSubtaskRequest request = CreateSubtaskRequest.builder()
														   .titulo("Sub 1")
														   .descricao("Desc")
														   .tarefaId(tarefaId)
														   .build();

		// when // then
		mockMvc.perform(post("/tarefas/" + tarefaId + "/subtarefas").contentType(MediaType.APPLICATION_JSON)
																	.content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.id").exists())
			   .andExpect(jsonPath("$.titulo", is("Sub 1")))
			   .andExpect(jsonPath("$.status", is(SubtaskStatus.PENDENTE.name())));
	}

	@Test
	@DisplayName("Listar subtarefas de uma tarefa")
	void shouldListSubtasksByTask() throws Exception {
		// given
		UUID tarefaId = UUID.randomUUID();
		SubtaskModel sub1 = new SubtaskModel(UUID.randomUUID(),
											 "Sub 1",
											 "Desc",
											 SubtaskStatus.PENDENTE,
											 LocalDateTime.now(),
											 null,
											 tarefaId);
		SubtaskModel sub2 = new SubtaskModel(UUID.randomUUID(),
											 "Sub 2",
											 "Desc",
											 SubtaskStatus.CONCLUIDA,
											 LocalDateTime.now(),
											 LocalDateTime.now(),
											 tarefaId);
		subtaskRepository.save(sub1);
		subtaskRepository.save(sub2);

		// when // then
		mockMvc.perform(get("/tarefas/" + tarefaId + "/subtarefas"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$", hasSize(2)))
			   .andExpect(jsonPath("$[0].tarefaId", is(tarefaId.toString())));
	}

	@Test
	@DisplayName("Atualizar status da subtarefa para CONCLUIDA via controller")
	void shouldUpdateSubtaskStatusToCompletedInController() throws Exception {
		// given
		UUID tarefaId = UUID.randomUUID();
		SubtaskModel sub = new SubtaskModel(UUID.randomUUID(),
											"Sub 1",
											"Desc",
											SubtaskStatus.EM_ANDAMENTO,
											LocalDateTime.now(),
											null,
											tarefaId);
		subtaskRepository.save(sub);

		// when // then
		mockMvc.perform(patch("/subtarefas/" + sub.getId() + "/status").param("status", SubtaskStatus.CONCLUIDA.name()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.status", is(SubtaskStatus.CONCLUIDA.name())));
	}
}

