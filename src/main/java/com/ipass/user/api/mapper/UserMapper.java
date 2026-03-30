package com.ipass.user.api.mapper;

import com.ipass.user.api.dto.request.CreateUserRequest;
import com.ipass.user.api.dto.response.UserResponse;
import com.ipass.user.command.CreateUserCommand;
import com.ipass.user.domain.UserModel;

public final class UserMapper {

	private UserMapper() {}

	public static CreateUserCommand toCoomand(CreateUserRequest dto) {
		return CreateUserCommand.builder().name(dto.getName()).email(dto.getEmail()).build();
	}

	public static UserResponse toResponse(UserModel user) {
		return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
	}
}
