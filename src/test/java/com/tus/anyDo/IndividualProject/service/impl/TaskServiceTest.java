package com.tus.anyDo.IndividualProject.service.impl;

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

import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.dao.TaskRepository;
import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.dto.ManagerAssignRequest;
import com.tus.anyDo.IndividualProject.dto.ManagerAssignResponse;
import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.ProjectNotFoundException;
import com.tus.anyDo.IndividualProject.exception.TaskNotFoundException;
import com.tus.anyDo.IndividualProject.exception.UnauthorizedAccessToTaskException;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.model.Role;

class TaskServiceTest {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;
    
    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        taskRepository = mock(TaskRepository.class);
        projectRepository = mock(ProjectRepository.class);
        
        taskService = new TaskService(userRepository, taskRepository, projectRepository);
    }
    
    // createTask tests
    @Test
    void testUserNotFoundCreatingTask() {
        final String USERNAME = "invalidUsername";
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
                taskService.createTask(taskCreateRequest, USERNAME);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
        verify(taskRepository, new Times(0)).save(any());
    }
    
    @Test
    void testProjectNotFoundCreatingTask() {
        final String USERNAME = "username";
        final User user = new User();
        user.setUsername(USERNAME);
        
        final Long PROJECT_ID = 1L;
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setProjectId(PROJECT_ID);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
            taskService.createTask(taskCreateRequest, USERNAME);
        });
        
        assertEquals("Project with id: " + PROJECT_ID + " does not exist.", e.getMessage());
        verify(taskRepository, new Times(0)).save(any());
    }
    
    @Test
    void testSuccessfullyCreateTaskWithProject() throws UserNotFoundException, ProjectNotFoundException {
        final String USERNAME = "username";
        final User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        
        final Long PROJECT_ID = 1L;
        final Project project = new Project();
        project.setId(PROJECT_ID);
        
        final String TASK_NAME = "taskName";
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setProjectId(PROJECT_ID);
        taskCreateRequest.setName(TASK_NAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        
        TaskResponseDto response = taskService.createTask(taskCreateRequest, USERNAME);
        
        assertEquals(TASK_NAME, response.getTaskName());
        verify(taskRepository, new Times(1)).save(any());
    }
    
    @Test
    void testSuccessfullyCreateTaskWithoutProject() throws UserNotFoundException, ProjectNotFoundException {
        final String USERNAME = "username";
        final User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        
        final String TASK_NAME = "taskName";
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setName(TASK_NAME);
        taskCreateRequest.setProjectId(null);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        
        TaskResponseDto response = taskService.createTask(taskCreateRequest, USERNAME);
        
        assertEquals(TASK_NAME, response.getTaskName());
        verify(taskRepository, new Times(1)).save(any());
    }
    
    // getTasksByUsername tests
    @Test
    void testUserNotFoundGettingTasksByUsername() {
        final String USERNAME = "invalidUsername";
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
                taskService.getTasksByUsername(USERNAME);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
    }
    
    @Test
    void testGetTasksByUsernameNone() throws UserNotFoundException {
        final String USERNAME = "username";
        final User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(user.getId())).thenReturn(new ArrayList<>());
        
        List<TaskResponseDto> tasks = taskService.getTasksByUsername(USERNAME);
        
        assertTrue(tasks.isEmpty());
    }
    
    @Test
    void testGetTasksByUsernameMany() throws UserNotFoundException {
        final String USERNAME = "username";
        final User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        
        final List<Task> userTasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTaskName("task1");
        task1.setCreator(user);
        task1.setUser(user);
        userTasks.add(task1);
        
        Task task2 = new Task();
        task2.setTaskName("task2");
        task2.setCreator(user);
        task2.setUser(user);
        userTasks.add(task2);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(user.getId())).thenReturn(userTasks);
        
        List<TaskResponseDto> tasks = taskService.getTasksByUsername(USERNAME);
        
        assertEquals(2, tasks.size());
    }
    
    // deleteTask tests
    @Test
    void testTaskNotFoundWhenDeleting() {
        final String USERNAME = "username";
        final long TASK_ID = 1L;
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(USERNAME, TASK_ID);
        });
        
        assertEquals("Task with id: " + TASK_ID + " cannot be found.", e.getMessage());
        verify(taskRepository, new Times(0)).delete(any());
    }
    
    @Test
    void testUnauthorizedAccessWhenDeletingTask() {
        final String USERNAME = "username";
        final String DIFFERENT_USERNAME = "differentUser";
        final long TASK_ID = 1L;
        
        User creator = new User();
        creator.setUsername(DIFFERENT_USERNAME);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(creator);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        
        Throwable e = assertThrows(UnauthorizedAccessToTaskException.class, () -> {
            taskService.deleteTask(USERNAME, TASK_ID);
        });
        
        assertEquals("You do not have access to task with id: " + TASK_ID, e.getMessage());
        verify(taskRepository, new Times(0)).delete(any());
    }
    
    @Test
    void testSuccessfullyDeleteTask() throws TaskNotFoundException, UnauthorizedAccessToTaskException {
        final String USERNAME = "username";
        final long TASK_ID = 1L;
        
        User creator = new User();
        creator.setUsername(USERNAME);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(creator);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        
        taskService.deleteTask(USERNAME, TASK_ID);
        
        verify(taskRepository, new Times(1)).delete(task);
    }
    
    // getTaskById tests
    @Test
    void testTaskNotFoundWhenGettingById() {
        final String USERNAME = "username";
        final long TASK_ID = 1L;
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(USERNAME, TASK_ID);
        });
        
        assertEquals("Task with id: " + TASK_ID + " cannot be found.", e.getMessage());
    }
    
    @Test
    void testUnauthorizedAccessWhenGettingTaskById() {
        final String USERNAME = "username";
        final String DIFFERENT_USERNAME = "differentUser";
        final long TASK_ID = 1L;
        
        User creator = new User();
        creator.setUsername(DIFFERENT_USERNAME);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(creator);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        
        Throwable e = assertThrows(UnauthorizedAccessToTaskException.class, () -> {
            taskService.getTaskById(USERNAME, TASK_ID);
        });
        
        assertEquals("You do not have access to task with id: " + TASK_ID, e.getMessage());
    }
    
    @Test
    void testSuccessfullyGetTaskById() throws TaskNotFoundException, UnauthorizedAccessToTaskException {
        final String USERNAME = "username";
        final long TASK_ID = 1L;
        final String TASK_NAME = "taskName";
        
        User creator = new User();
        creator.setUsername(USERNAME);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(creator);
        task.setUser(creator);
        task.setTaskName(TASK_NAME);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        
        TaskResponseDto response = taskService.getTaskById(USERNAME, TASK_ID);
        
        assertEquals(TASK_NAME, response.getTaskName());
    }
    
    // updateTask tests
    @Test
    void testUserNotFoundWhenUpdatingTask() {
        final String USERNAME = "invalidUser";
        final long TASK_ID = 1L;
        final TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
            taskService.updateTask(USERNAME, TASK_ID, updateRequest);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
    }
    
    @Test
    void testTaskNotFoundWhenUpdating() {
        final String USERNAME = "username";
        final User user = new User();
        user.setUsername(USERNAME);
        
        final long TASK_ID = 1L;
        final TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(USERNAME, TASK_ID, updateRequest);
        });
        
        assertEquals("Task with id: " + TASK_ID + " cannot be found.", e.getMessage());
    }
    
    @Test
    void testUnauthorizedAccessWhenUpdatingTask() {
        final String USERNAME = "username";
        final String DIFFERENT_USERNAME = "differentUser";
        final long TASK_ID = 1L;
        
        User creator = new User();
        creator.setUsername(DIFFERENT_USERNAME);
        
        User currentUser = new User();
        currentUser.setUsername(USERNAME);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(creator);
        
        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        
        Throwable e = assertThrows(UnauthorizedAccessToTaskException.class, () -> {
            taskService.updateTask(USERNAME, TASK_ID, updateRequest);
        });
        
        assertEquals("You do not have access to task with id: " + TASK_ID, e.getMessage());
    }
    
    @Test
    void testProjectNotFoundWhenUpdatingTask() {
        final String USERNAME = "username";
        final User user = new User();
        user.setUsername(USERNAME);
        
        final long TASK_ID = 1L;
        final Long PROJECT_ID = 2L;
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(user);
        
        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setProjectId(PROJECT_ID);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
            taskService.updateTask(USERNAME, TASK_ID, updateRequest);
        });
        
        assertEquals("Project with id: " + PROJECT_ID + " does not exist.", e.getMessage());
    }
    
    @Test
    void testSuccessfullyUpdateTask() throws UnauthorizedAccessToTaskException, UserNotFoundException, ProjectNotFoundException, TaskNotFoundException {
        final String USERNAME = "username";
        final User user = new User();
        user.setUsername(USERNAME);
        
        final long TASK_ID = 1L;
        final Long PROJECT_ID = 2L;
        final String UPDATED_TASK_NAME = "updatedTaskName";
        
        Project project = new Project();
        project.setId(PROJECT_ID);
        
        Task task = new Task();
        task.setId(TASK_ID);
        task.setCreator(user);
        
        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setProjectId(PROJECT_ID);
        updateRequest.setTaskName(UPDATED_TASK_NAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        
        TaskResponseDto response = taskService.updateTask(USERNAME, TASK_ID, updateRequest);
        
        assertEquals(UPDATED_TASK_NAME, response.getTaskName());
        verify(taskRepository, new Times(1)).save(any());
    }
    
    // assignTask tests
    @Test
    void testManagerNotFoundWhenAssigningTask() {
        final String USERNAME = "invalidManager";
        final ManagerAssignRequest request = new ManagerAssignRequest();
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
            taskService.assignTask(USERNAME, request);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
    }
    
    @Test
    void testAssignedUserNotFoundWhenAssigningTask() {
        final String USERNAME = "manager";
        final User manager = new User();
        manager.setUsername(USERNAME);
        
        final String ASSIGNED_USERNAME = "invalidUser";
        final ManagerAssignRequest request = new ManagerAssignRequest();
        request.setAssignedUser(ASSIGNED_USERNAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(manager));
        when(userRepository.findByUsername(ASSIGNED_USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
            taskService.assignTask(USERNAME, request);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
    }
    
    @Test
    void testProjectNotFoundWhenAssigningTask() {
        final String USERNAME = "manager";
        final User manager = new User();
        manager.setUsername(USERNAME);
        
        final String ASSIGNED_USERNAME = "user";
        final User assignedUser = new User();
        assignedUser.setUsername(ASSIGNED_USERNAME);
        
        final Long PROJECT_ID = 1L;
        final ManagerAssignRequest request = new ManagerAssignRequest();
        request.setAssignedUser(ASSIGNED_USERNAME);
        request.setProjectId(PROJECT_ID);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(manager));
        when(userRepository.findByUsername(ASSIGNED_USERNAME)).thenReturn(Optional.of(assignedUser));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(ProjectNotFoundException.class, () -> {
            taskService.assignTask(USERNAME, request);
        });
        
        assertEquals("Project with id: " + PROJECT_ID + " does not exist.", e.getMessage());
    }
    
    @Test
    void testSuccessfullyAssignTask() throws ProjectNotFoundException, UserNotFoundException {
        final String USERNAME = "manager";
        final User manager = new User();
        manager.setUsername(USERNAME);
        
        final String ASSIGNED_USERNAME = "user";
        final User assignedUser = new User();
        assignedUser.setUsername(ASSIGNED_USERNAME);
        
        final Long PROJECT_ID = 1L;
        final Project project = new Project();
        project.setId(PROJECT_ID);
        project.setProjectName("projectName");
        
        final String TASK_NAME = "taskName";
        final ManagerAssignRequest request = new ManagerAssignRequest();
        request.setAssignedUser(ASSIGNED_USERNAME);
        request.setProjectId(PROJECT_ID);
        request.setTaskName(TASK_NAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(manager));
        when(userRepository.findByUsername(ASSIGNED_USERNAME)).thenReturn(Optional.of(assignedUser));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        
        ManagerAssignResponse response = taskService.assignTask(USERNAME, request);
        
        assertEquals(TASK_NAME, response.getTaskName());
        assertEquals(ASSIGNED_USERNAME, response.getAssignedTo());
        verify(taskRepository, new Times(1)).save(any());
    }
    
    // getManagerTasks tests
    @Test
    void testManagerNotFoundWhenGettingManagerTasks() {
        final String USERNAME = "invalidManager";
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
            taskService.getManagerTasks(USERNAME);
        });
        
        assertEquals("User with username: " + USERNAME + " does not exist.", e.getMessage());
    }
    
    @Test
    void testGetManagerTasksNone() throws UserNotFoundException {
        final String USERNAME = "manager";
        final User manager = new User();
        manager.setUsername(USERNAME);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(manager));
        when(taskRepository.findByCreator(manager)).thenReturn(new ArrayList<>());
        
        List<TaskResponseDto> tasks = taskService.getManagerTasks(USERNAME);
        
        assertTrue(tasks.isEmpty());
    }
    
    @Test
    void testGetManagerTasksMany() throws UserNotFoundException {
        final String USERNAME = "manager";
        final User manager = new User();
        manager.setUsername(USERNAME);
        manager.setRole(Role.ROLE_PROJECT_MANAGER);
        
        final User user = new User();
        user.setUsername(USERNAME);
        user.setRole(Role.ROLE_TEAMWORKER);
        
        List<Task> managerTasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTaskName("task1");
        task1.setCreator(manager);
        task1.setUser(user);
        managerTasks.add(task1);
        
        Task task2 = new Task();
        task2.setTaskName("task2");
        task2.setCreator(manager);
        task2.setUser(user);
        managerTasks.add(task2);
        
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(manager));
        when(taskRepository.findByCreator(manager)).thenReturn(managerTasks);
        
        List<TaskResponseDto> tasks = taskService.getManagerTasks(USERNAME);
        
        assertEquals(2, tasks.size());
    }
}