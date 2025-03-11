package com.tus.anyDo.IndividualProject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequest {
	@NotEmpty(message = "Project name must be provided.")
	private String projectName;
}
