package com.tus.any_do.individual_project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectUpdateRequest {
	@NotEmpty(message = "Project Name cannot be null")
	private String projectName;
}
