package com.ipass.user.service;

import com.ipass.user.command.CreateUserCommand;
import com.ipass.user.domain.UserModel;
import com.ipass.user.exception.UserEmailAlreadyInUseException;
import com.ipass.user.exception.UserNotFoundException;
import com.ipass.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("Criar usuário com sucesso no service")
	void shouldCreateUserSuccessfullyInService() {
		// given
		CreateUserCommand cmd = CreateUserCommand.builder().name("Kanamy").email("kanamy@example.com").build();

		when(userRepository.existsByEmail(cmd.getEmail())).thenReturn(false);

		UUID id = UUID.randomUUID();
		UserModel saved = new UserModel(id, cmd.getName(), cmd.getEmail(), LocalDateTime.now());
		when(userRepository.save(Mockito.any(UserModel.class))).thenReturn(saved);

		// when
		UserModel result = userService.handle(cmd);

		// then
		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals(cmd.getName(), result.getName());
		assertEquals(cmd.getEmail(), result.getEmail());
	}

	@Test
	@DisplayName("Não criar usuário quando email já está em uso")
	void shouldNotCreateUserWhenEmailAlreadyInUse() {
		// given
		CreateUserCommand cmd = CreateUserCommand.builder().name("Kanamy").email("Kanamy@example.com").build();

		when(userRepository.existsByEmail(cmd.getEmail())).thenReturn(true);

		// when // then
		assertThrows(UserEmailAlreadyInUseException.class, () -> userService.handle(cmd));
	}

	@Test
	@DisplayName("Buscar usuário por ID com sucesso no service")
	void shouldFindUserByIdSuccessfullyInService() {
		// given
		UUID id = UUID.randomUUID();
		UserModel user = new UserModel(id, "Beltrano", "beltrano@example.com", LocalDateTime.now());
		when(userRepository.findByIdOrThrowNotFound(id)).thenReturn(user);

		// when
		UserModel result = userService.findById(id);

		// then
		assertNotNull(result);
		assertEquals(id, result.getId());
	}

	@Test
	@DisplayName("Lançar exceção quando usuário não encontrado no service")
	void shouldThrowWhenUserNotFoundInService() {
		// given
		UUID id = UUID.randomUUID();
		when(userRepository.findByIdOrThrowNotFound(id)).thenThrow(new UserNotFoundException());

		// when // then
		assertThrows(UserNotFoundException.class, () -> userService.findById(id));
	}
}
