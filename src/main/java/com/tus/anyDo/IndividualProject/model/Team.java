package com.tus.anyDo.IndividualProject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data  // Lombok annotation to automatically generate getters, setters, toString, etc.
@NoArgsConstructor  // Lombok annotation to generate the no-args constructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @ManyToMany
    @JoinTable(
    	name="team_user",
    	joinColumns = @JoinColumn(name = "team_id"),
    	inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> teamMembers;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // Constructor with teamName, teamMembers, and creator to make object creation easier
    public Team(String teamName, List<User> teamMembers, User creator) {
        this.teamName = teamName;
        this.teamMembers = teamMembers;
        this.creator = creator;
    }
}
