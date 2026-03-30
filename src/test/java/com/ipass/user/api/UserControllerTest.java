package com.ipass.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipass.user.api.dto.request.CreateUserRequest;
import com.ipass.user.domain.UserModel;
import com.ipass.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserRepository userRepository;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		this.objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("Criar um usuário com sucesso")
	void shouldCreateUserSuccessfully() throws Exception {
		// given
		CreateUserRequest request = CreateUserRequest.builder()
													 .name("Kanamy Stewart")
													 .email("kanamystewart@example.com")
													 .build();

		// when // then
		mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
										 .content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.id").exists())
			   .andExpect(jsonPath("$.name", is("Kanamy Stewart")))
			   .andExpect(jsonPath("$.email", is("kanamystewart@example.com")));
	}

	@Test
	@DisplayName("Buscar um usuário com sucesso")
	void shouldGetUserSuccessfully() throws Exception {
		// given
		UUID id = UUID.randomUUID();
		UserModel model = new UserModel(id, "Stewart", "stewart@example.com", LocalDateTime.now());
		userRepository.save(model);

		// when // then
		mockMvc.perform(get("/usuarios/" + id))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.id").value(id.toString()))
			   .andExpect(jsonPath("$.name", is("Stewart")))
			   .andExpect(jsonPath("$.email", is("stewart@example.com")));
	}

	@Test
	@DisplayName("Não achar usuário que não existe")
	void shouldReturnErrorWhenUserNotFound() throws Exception {
		// given
		UUID id = UUID.randomUUID();

		// when // then
		mockMvc.perform(get("/usuarios/" + id)).andExpect(status().isBadRequest());
	}
}
