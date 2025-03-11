package com.tus.anyDo.IndividualProject.service.impl;

import com.tus.anyDo.IndividualProject.dao.TeamRepository;
import com.tus.anyDo.IndividualProject.model.Team;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.ITeamService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService implements ITeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team createTeam(String teamName, List<User> teamMembers, User creator) {
        // Create a new Team and set its properties
        Team team = new Team();
        team.setTeamName(teamName);
        team.setTeamMembers(teamMembers);

        team.setCreator(creator);

        // Save the team to the repository
        return teamRepository.save(team);
    }
}
