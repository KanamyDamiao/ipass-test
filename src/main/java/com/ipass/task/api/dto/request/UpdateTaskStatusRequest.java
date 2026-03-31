package com.ipass.task.api.dto.request;

import com.ipass.task.domain.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskStatusRequest {

	@NotNull(message = "{UpdateTaskStatusRequest.status.NotNull}")
	private TaskStatus status;
}

