package com.tus.any_do.individual_project.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import com.tus.any_do.individual_project.dao.ProjectRepository;
import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.dto.ProjectCreateRequest;
import com.tus.any_do.individual_project.dto.ProjectResponseDto;
import com.tus.any_do.individual_project.dto.ProjectUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToProjectException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;
import com.tus.any_do.individual_project.model.Project;
import com.tus.any_do.individual_project.model.Role;
import com.tus.any_do.individual_project.model.User;
import com.tus.any_do.individual_project.service.impl.ProjectService;

class ProjectServiceTest {
	private UserRepository userRepository;
	private ProjectRepository projectRepository;
	private ProjectService projectService;
	
	@BeforeEach
	void setup() {
		userRepository = mock(UserRepository.class);
		projectRepository = mock(ProjectRepository.class);
		
		projectService = new ProjectService(userRepository, projectRepository);
	}
	
	
	// createProject
	@Test
	void testUserNotFoundCreatingProject() {
		final String USERNAME = "invalidUsername";
		final String PROJECT_NAME = "projectName";
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(UserNotFoundException.class, () -> {
				projectService.createProject(projectCreateRequest, USERNAME);
		});
		
		assertEquals("Cannot find User with username: " + USERNAME, e.getMessage());
		verify(projectRepository, new Times(0)).save(any());
	}
	
	@Test
	void testProjectSuccessfullyCreated() throws UserNotFoundException {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.save(any())).thenReturn(expectedProject);
		
		Project actualProject = projectService.createProject(projectCreateRequest, USERNAME);
		
		assertEquals(USERNAME, actualProject.getCreator().getUsername());
		assertEquals(PASSWORD, actualProject.getCreator().getPassword());
		assertEquals(PROJECT_NAME, actualProject.getProjectName());
		assertTrue(actualProject.getTasks().isEmpty());
		verify(projectRepository, new Times(1)).save(any());
	}
	
	
	
	// getProjectById
	@Test
	void testUserNotFoundGettingProjectById() {
		final String USERNAME = "invalidUsername";
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(UserNotFoundException.class, () -> {
				projectService.getProjectById(projectId, USERNAME);
		});
		
		assertEquals("Cannot find User with username: " + USERNAME, e.getMessage());
		verify(projectRepository, new Times(0)).findById(any());
	}
	
	@Test
	void testProjectNotFoundGettingProjectById() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
			projectService.getProjectById(projectId, USERNAME);
		});
		
		assertEquals("Project with ID: " + projectId + " does not exist.", e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
	}
	
	@Test
	void testUnauthorizedAccessToProject() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String DIFFERENT_USERNAME = "differentUsername";
		final User differentUser = new User();
		differentUser.setId(2L);
		differentUser.setUsername(DIFFERENT_USERNAME);
		differentUser.setPassword(PASSWORD);
		differentUser.setRole(role);
		
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(differentUser);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		Throwable e = assertThrows(UnauthorizedAccessToProjectException.class, () -> {
			projectService.getProjectById(projectId, USERNAME);
		});
		
		assertEquals("You do not have access to the project with id: " + projectId, e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
	}
	
	@Test
	void testSuccessfullyGetProjectById() throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 2L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		ProjectResponseDto actualProject = projectService.getProjectById(projectId, USERNAME);
		
		assertEquals(PROJECT_NAME, actualProject.getProjectName());
	}
	
	
	// deleteProject
	@Test
	void testUserNotFoundDeletingProjectById() {
		final String USERNAME = "invalidUsername";
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(UserNotFoundException.class, () -> {
				projectService.deleteProject(projectId, USERNAME);
		});
		
		assertEquals("Cannot find User with username: " + USERNAME, e.getMessage());
		verify(projectRepository, new Times(0)).findById(any());
		verify(projectRepository, new Times(0)).deleteById(any());
	}
	
	@Test
	void testProjectNotFoundDeletingProjectById() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
			projectService.deleteProject(projectId, USERNAME);
		});
		
		assertEquals("Project with ID: " + projectId + " does not exist.", e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
		verify(projectRepository, new Times(0)).deleteById(any());
	}
	
	
	@Test
	void testUnauthorizedAccessToProjectWhenDeletingById() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String DIFFERENT_USERNAME = "differentUsername";
		final User differentUser = new User();
		differentUser.setId(2L);
		differentUser.setUsername(DIFFERENT_USERNAME);
		differentUser.setPassword(PASSWORD);
		differentUser.setRole(role);
		
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(differentUser);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		Throwable e = assertThrows(UnauthorizedAccessToProjectException.class, () -> {
			projectService.deleteProject(projectId, USERNAME);
		});
		
		assertEquals("You do not have access to the project with id: " + projectId, e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
		verify(projectRepository, new Times(0)).deleteById(any());
	}
	
	
	@Test
	void testSuccessfullyDeleteProjectById() throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 2L;
		final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
		projectCreateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		projectService.deleteProject(projectId, USERNAME);
		
		verify(projectRepository, new Times(1)).deleteById(any());
	}
	
	
	// updateProject
	@Test
	void testUserNotFoundUpdatingProjectById() {
		final String USERNAME = "invalidUsername";
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
		projectUpdateRequest.setProjectName(PROJECT_NAME);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(UserNotFoundException.class, () -> {
				projectService.updateProject(projectId, projectUpdateRequest, USERNAME);
		});
		
		assertEquals("Cannot find User with username: " + USERNAME, e.getMessage());
		verify(projectRepository, new Times(0)).findById(any());
		verify(projectRepository, new Times(0)).save(any());
	}
	
	@Test
	void testProjectNotFoundUpdatingProjectById() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
		projectUpdateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
			projectService.updateProject(projectId, projectUpdateRequest, USERNAME);
		});
		
		assertEquals("Project with ID: " + projectId + " does not exist.", e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
		verify(projectRepository, new Times(0)).deleteById(any());
	}
	
	
	@Test
	void testUnauthorizedAccessToProjectWhenUpdatingById() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String DIFFERENT_USERNAME = "differentUsername";
		final User differentUser = new User();
		differentUser.setId(2L);
		differentUser.setUsername(DIFFERENT_USERNAME);
		differentUser.setPassword(PASSWORD);
		differentUser.setRole(role);
		
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 1L;
		final ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
		projectUpdateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(differentUser);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		Throwable e = assertThrows(UnauthorizedAccessToProjectException.class, () -> {
			projectService.updateProject(projectId, projectUpdateRequest, USERNAME);
		});
		
		assertEquals("You do not have access to the project with id: " + projectId, e.getMessage());
		verify(projectRepository, new Times(1)).findById(any());
		verify(projectRepository, new Times(0)).deleteById(any());
	}
	
	
	@Test
	void testSuccessfullyUpdateProjectById() throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final String PROJECT_NAME = "projectName";
		final long projectId = 2L;
		final ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
		projectUpdateRequest.setProjectName(PROJECT_NAME);
		
		final Project expectedProject = new Project();
		expectedProject.setId(projectId);
		expectedProject.setCreator(user);
		expectedProject.setProjectName(PROJECT_NAME);
		expectedProject.setTasks(new ArrayList<>());
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
		when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));
		
		ProjectResponseDto projectResponseDto = projectService.updateProject(projectId, projectUpdateRequest, USERNAME);
		
		assertEquals(projectId, projectResponseDto.getProjectId());
		assertEquals(PROJECT_NAME, projectResponseDto.getProjectName());
		verify(projectRepository, new Times(1)).save(any());
	}
	
	
	// getAllMyProjects
	@Test
	void testGetAllMyProjectsNone() {
		final String USERNAME = "username";
		final List<Project> myProjects = new ArrayList<>();
		when(projectRepository.findByCreator_Username(USERNAME)).thenReturn(myProjects);
		
		List<ProjectResponseDto> allMyProjects = projectService.getAllMyProjects(USERNAME);
		
		assertEquals(0, allMyProjects.size());
	}
	
	@Test
	void testGetAllMyProjectsMany() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_PROJECT_MANAGER;
		final User user = new User();
		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setRole(role);
		
		final List<Project> myProjects = new ArrayList<>();
		
		final String PROJECT_NAME1 = "projectName";
		final Project project1 = new Project();
		project1.setCreator(user);
		project1.setId(2L);
		project1.setProjectName(PROJECT_NAME1);
		project1.setTasks(new ArrayList<>());
		myProjects.add(project1);
		
		final String PROJECT_NAME2 = "projectName";
		final Project project2 = new Project();
		project2.setCreator(user);
		project2.setId(3L);
		project2.setProjectName(PROJECT_NAME2);
		project2.setTasks(new ArrayList<>());
		myProjects.add(project2);
		
		when(projectRepository.findByCreator_Username(USERNAME)).thenReturn(myProjects);
		
		List<ProjectResponseDto> allMyProjects = projectService.getAllMyProjects(USERNAME);
		
		assertEquals(2, allMyProjects.size());
		List<String> responseProjectNames = allMyProjects.stream().map(ProjectResponseDto::getProjectName).toList();
		assertTrue(responseProjectNames.contains(PROJECT_NAME1));
		assertTrue(responseProjectNames.contains(PROJECT_NAME2));
	}
	
}
