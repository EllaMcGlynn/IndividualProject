$(document).ready(function() {
    const projectTableBody = $("#project-table tbody");
    const taskTableBody = $("#task-table tbody");

    // Open New Project Modal
    $("#openProjectModal").on("click", function() {
        const myModal = new bootstrap.Modal($("#newProjectModal")[0]);
        myModal.show();
    });

    // Open New Task Modal
	$("#openCreateTasktModal").on("click", function () {
	        populateProjecteDropdown();
			populateUsersDropdown();
	        const myModal = new bootstrap.Modal($("#newCreateTaskModal")[0]);
	        myModal.show();
	    });

    // Event delegation for delete buttons in project table
    projectTableBody.on("click", ".delete-project", function() {
        const projectId = $(this).data("id");
        openDeleteProjectModal(projectId);
    });

    // Event delegation for delete buttons in task table
    taskTableBody.on("click", ".delete-task", function() {
        const taskId = $(this).data("id");
        openDeleteTaskModal(taskId);
    });

    loadProjects();
    loadTasks();

    // Update the Date Card with current date
    const currentDate = new Date();
    const formattedDate = currentDate.toLocaleDateString("en-GB", {
        weekday: "long", // Optional: Display the weekday (e.g., "Monday")
        day: "numeric",  // Day of the month (e.g., "11")
        month: "long",   // Full month name (e.g., "March")
        year: "numeric"  // Year (e.g., "2025")
    });

    // Update the 'cardName' with the current date
    $(".numbers#currentDate").text(formattedDate);
	
	
	// Delete button
	$(document).on("click", ".delete-project", function () {
	    let projectId = $(this).attr("data-id");
	    openDeleteProjectModal(projectId);
	});
});


// Create New Project
function createProject() {
    const projectData = {
        projectName: $("#project-name").val(),
    };

    $.ajax({
        url: "/api/projects",
        type: "POST",
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        contentType: "application/json",
        data: JSON.stringify(projectData),
        success: function() {
            $("#newProjectModal").modal("hide");
            $("#newProjectModal form").trigger("reset");
            loadProjects();
        },
        error: function() {
            showErrorMessage("Error creating project, please try again.");
        },
    });
}

// Create New Task
function createTask() {
    const taskData = {
        name: $("#task-name").val(),
        projectId: $("#assign-project").val() || null,
        assignedPerson: $("#assign-person").val() || null,
    };

    $.ajax({
        url: "/api/tasks/add",
        type: "POST",
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        contentType: "application/json",
        data: JSON.stringify(taskData),
        success: function() {
            $("#newCreateTaskModal").modal("hide");
            $("#newCreateTaskModal form").trigger("reset");
            loadTasks();
        },
        error: function() {
            showErrorMessage("Error creating task, please try again.");
        },
    });
}

// Load Projects
function loadProjects() {
    $.ajax({
        url: "/api/projects",
        type: "GET",
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        success: function(response) {
            renderProjects(response);
        },
        error: function() {
            showErrorMessage("Error loading projects, please try again.");
        },
    });
}

// Load Tasks
function loadTasks() {
    $.ajax({
        url: "/api/tasks/list",
        type: "GET",
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        success: function(response) {
            renderTasks(response);
        },
        error: function() {
            showErrorMessage("Error loading tasks, please try again.");
        },
    });
}

// Render Projects
function renderProjects(projects) {
    const projectTableBody = $("#project-table tbody");
    projectTableBody.empty();

    projects.forEach((project) => {
        const row = `
            <tr>
                <td>${project.projectName}</td>
                <td>
                    <button class="btn btn-sm btn-danger delete-project" data-id="${project.projectId}">Delete</button>
                </td>
            </tr>`;
        projectTableBody.append(row);
    });
}

// Render Tasks
function renderTasks(tasks) {
    const taskTableBody = $("#task-table tbody");
    taskTableBody.empty();

    tasks.forEach((task) => {
        const row = `
            <tr>
                <td>${task.name}</td>
                <td>${task.projectId || ""}</td>
                <td>${task.assignedPerson || ""}</td>
                <td>
                    <button class="btn btn-sm btn-danger delete-task" data-id="${task.id}">Delete</button>
                </td>
            </tr>`;
        taskTableBody.append(row);
    });
}

// Open Delete Project Modal
function openDeleteProjectModal(projectId) {
    $('#deleteProjectModal').data('projectId', projectId).modal('show');
}

// Open Delete Task Modal
function openDeleteTaskModal(taskId) {
    $('#deleteTaskModal').data('taskId', taskId).modal('show');
}

// Handle Project Deletion
$('#confirmDeleteProjectBtn').on('click', function() {
    const projectIdToDelete = $('#deleteProjectModal').data('projectId');

    if (projectIdToDelete !== undefined) {
        $.ajax({
            url: `/api/projects/${projectIdToDelete}`,
            type: 'DELETE',
            headers: { 'Authorization': `Bearer ${JwtStorage.getJwt()}` },
            success: function() {
                $('#deleteProjectModal').modal('hide');
                loadProjects();
				$('#deleteProjectModal').modal('hide');
            },
            error: function() {
                showErrorMessage('Error deleting project, please try again.');
                $('#deleteProjectModal').modal('hide');
            }
        });
    } else {
        showErrorMessage('No project selected for deletion.');
    }
});

// Handle Task Deletion
$('#confirmDeleteTaskBtn').on('click', function() {
    const taskIdToDelete = $('#deleteTaskModal').data('taskId');

    if (taskIdToDelete !== undefined) {
        $.ajax({
            url: `/api/tasks/${taskIdToDelete}`,
            type: 'DELETE',
            headers: { 'Authorization': `Bearer ${JwtStorage.getJwt()}` },
            success: function() {
                $('#deleteTaskModal').modal('hide');
                loadTasks();
            },
            error: function() {
                showErrorMessage('Error deleting task, please try again.');
                $('#deleteTaskModal').modal('hide');
            }
        });
    } else {
        showErrorMessage('No task selected for deletion.');
    }
});

// Display error message
function showErrorMessage(message) {
    alert(message);
}

function populateProjecteDropdown() {
    $.ajax({
        url: "/api/projects",
		headers: { 'Authorization': `Bearer ${JwtStorage.getJwt()}` },
        type: "GET",
        dataType: "json",
        success: function (projects) {
            let dropdown = $("#assign-project"); 
            dropdown.empty(); 
            dropdown.append('<option value="">Select an option</option>'); 

            // Loop through the response and append options
            projects.forEach((project) => {
                dropdown.append(`<option value="${project.projectId}">${project.projectName}</option>`);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data:", error);
        }
    });
}

function populateUsersDropdown() {
	$.ajax({
        url: "/api/users",
		headers: { 'Authorization': `Bearer ${JwtStorage.getJwt()}` },
        type: "GET",
        dataType: "json",
        success: function (users) {
            let dropdown = $("#assign-person"); 
            dropdown.empty(); 
            dropdown.append('<option value="">Select an option</option>'); 

            // Loop through the response and append options
            users.forEach((user) => {
                dropdown.append(`<option value="${user.id}">${user.username}</option>`);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data:", error);
        }
    });
}
