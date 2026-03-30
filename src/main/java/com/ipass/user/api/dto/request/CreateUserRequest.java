package com.ipass.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

	@NotEmpty(message = "{CreateUserRequest.name.NotEmpty}")
	private String name;

	@NotEmpty(message = "{CreateUserRequest.email.NotEmpty}")
	@Email(message = "{CreateUserRequest.email.InvalidEmail}")
	private String email;
}
