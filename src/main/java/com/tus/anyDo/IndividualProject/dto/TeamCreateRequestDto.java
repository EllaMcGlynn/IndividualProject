package com.tus.anyDo.IndividualProject.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeamCreateRequestDto {
	
	private String teamName;
    private List<Long> memberNames;
}
