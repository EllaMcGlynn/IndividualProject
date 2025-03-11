package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private Long id;
    private String teamName;
    private String creatorUsername;
    private List<User> teamMembers;
}
