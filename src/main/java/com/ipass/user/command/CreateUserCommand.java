package com.ipass.user.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class CreateUserCommand {
	private String name;
	private String email;
}
