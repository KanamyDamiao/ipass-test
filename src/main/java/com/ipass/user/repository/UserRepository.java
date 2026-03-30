package com.ipass.user.repository;

import com.ipass.user.domain.UserModel;
import com.ipass.user.exception.UserNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	default UserModel findByIdOrThrowNotFound(UUID id) {
		return findById(id).orElseThrow(UserNotFoundException::new);
	}

	boolean existsByEmail(String email);
}
