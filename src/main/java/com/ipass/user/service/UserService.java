package com.ipass.user.service;

import com.ipass.user.command.CreateUserCommand;
import com.ipass.user.domain.UserModel;
import com.ipass.user.exception.UserEmailAlreadyInUseException;
import com.ipass.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserModel handle(CreateUserCommand cmd) {

		if (userRepository.existsByEmail(cmd.getEmail())) {
			throw new UserEmailAlreadyInUseException();
		}

		UserModel user = UserModel.builder().name(cmd.getName()).email(cmd.getEmail()).build();

		return userRepository.save(user);
	}

	public UserModel findById(UUID id) {
		return userRepository.findByIdOrThrowNotFound(id);
	}
}
