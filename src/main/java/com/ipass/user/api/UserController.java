package com.ipass.user.api;

import com.ipass.user.api.dto.request.CreateUserRequest;
import com.ipass.user.api.dto.response.UserResponse;
import com.ipass.user.api.mapper.UserMapper;
import com.ipass.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@Operation(description = "Cria um novo usuário", method = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Usuário não pode ser criado.") })
	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest dto) {

		var command = UserMapper.toCoomand(dto);

		var createdUser = service.handle(command);
		var response = UserMapper.toResponse(createdUser);

		return ResponseEntity.status(201).body(response);
	}

	@Operation(description = "Busca um usuário por ID", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Usuário não encontrado ou requisição inválida.") })
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
		var user = service.findById(id);
		var response = UserMapper.toResponse(user);
		return ResponseEntity.ok(response);
	}
}
