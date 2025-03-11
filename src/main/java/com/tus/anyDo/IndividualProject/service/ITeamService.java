package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.model.Team;
import com.tus.anyDo.IndividualProject.model.User;

import java.util.List;

public interface ITeamService {

    Team createTeam(String teamName, List<User> teamMembers, User creator);
}
