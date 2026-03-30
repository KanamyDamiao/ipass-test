package com.ipass.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

	@Id
	private UUID id;

	private String name;

	private String email;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public UserModel(String name, String email) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.email = email;
		this.createdAt = LocalDateTime.now();
	}

}
