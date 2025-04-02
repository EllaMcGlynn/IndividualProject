package com.tus.any_do.individual_project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequest {
	@NotEmpty(message = "Project name must be provided.")
	private String projectName;
}
