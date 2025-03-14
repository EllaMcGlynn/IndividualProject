$(document).ready(function() {
	const taskTableBody = $("#taskTable tbody");

	// Open New Task Modal
	$("#openTaskModal").on("click", function() {
		const myModal = new bootstrap.Modal($("#newTaskModal")[0]);
		myModal.show();
	});

	// Submit New Task Form
	$("#newTaskModal form").on("submit", function(event) {
		event.preventDefault();
		createTask();
	});

	// Event delegation for edit and delete buttons
	taskTableBody.on("click", ".edit-task", function() {
		const taskId = $(this).data("id");
		openEditTaskModal(taskId);
	});

	taskTableBody.on("click", ".delete-task", function() {
		const taskId = $(this).data("id");
		openDeleteTaskModal(taskId);
	});
	
	$('.modal-footer .btn-secondary').on('click', function() {
	    $('#deleteTaskModal').modal('hide');
	});


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
});

function createTask() {
	const taskData = {
		name: $("#taskName").val(),
		projectId: $("#taskProject").val() || null,
		status: $("#taskStatus").val(),
	};

	$.ajax({
		url: "api/tasks/add",
		type: "POST",
		headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
		contentType: "application/json",
		data: JSON.stringify(taskData),
		success: function() {
			$("#newTaskModal").modal("hide");
			$("#newTaskModal form").trigger("reset");
			loadTasks();
		},
		error: function() {
			showErrorMessage("Error creating task, please try again.");
		},
	});
}

function populateUncompletedTasksCount(tasks) {
	const uncompletedTasks = tasks.filter(task => task.status === "TODO" || task.status === "IN_PROGRESS");
	const uncompletedCount = uncompletedTasks.length;

	$("#uncompletedTasks").text(uncompletedCount);
}

function populateProjectCount(tasks) {
	// Get the unique projectIds from the tasks
	const projectIds = tasks.map(task => task.projectId).filter((value, index, self) => self.indexOf(value) === index && value != null);
	const projectCount = projectIds.length;

	// Update the 'numbers' div for total projects
	$("#projectAmount").text(projectCount);
}

function loadTasks() {
	$.ajax({
		url: "api/tasks/list",
		type: "GET",
		headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
		success: function(response) {
			console.log("API Response:", response);  // Debugging step
			renderTasks(response);

			populateUncompletedTasksCount(response);
			populateProjectCount(response);
			populateTaskChart(response);
		},
		error: function() {
			showErrorMessage("Error loading tasks, please try again.");
		},
	});
}

function renderTasks(tasks) {
	const taskTableBody = $("#taskTable tbody");
	taskTableBody.empty();

	tasks.forEach((task) => {
		const row = `
            <tr>
                <td>${task.taskName}</td>
                <td>${task.projectId || ""}</td>
                <td>${task.status}</td>
                <td>
                    <button class="btn btn-sm btn-primary edit-task" data-id="${task.id}">Edit</button>
                    <button class="btn btn-sm btn-danger delete-task" data-id="${task.id}">Delete</button>
                </td>
            </tr>`;
		taskTableBody.append(row);
	});
}

function showErrorMessage(message) {
	alert(message);
}

// Open Edit Task Modal with task data
function openEditTaskModal(taskId) {
    $.ajax({
        url: `/api/tasks/${taskId}`,
        type: 'GET',
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        success: function(task) {
            // Populate the form fields with the task data
            $("#editTaskName").val(task.taskName);
            $("#editTaskStatus").val(task.status);
            $("#editTaskModal").data('taskId', taskId);

            const myModal = new bootstrap.Modal($("#editTaskModal")[0]);
            myModal.show();
        },
        error: function() {
            showErrorMessage("Error loading task details.");
        }
    });
}

// Handle Task Update
$("#editTaskModal form").on("submit", function(event) {
    event.preventDefault();

    const taskId = $("#editTaskModal").data('taskId');
    const updatedTask = {
        taskName: $("#editTaskName").val(),
        status: $("#editTaskStatus").val(),
        // Include projectId if applicable
        projectId: $("#editTaskProject").val() || null
    };

    // Send an AJAX request to update the task
    $.ajax({
        url: `/api/tasks/update/${taskId}`,
        type: 'PUT',
        headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
        contentType: "application/json",
        data: JSON.stringify(updatedTask),
        success: function(response) {
            // Hide the modal and reset form
            $("#editTaskModal").modal("hide");
            loadTasks(); // Reload the tasks
        },
        error: function(xhr) {
            if (xhr.status === 404) {
                showErrorMessage("Task not found.");
            } else if (xhr.status === 403) {
                showErrorMessage("You do not have permission to update this task.");
            } else {
                showErrorMessage("Error updating task, please try again.");
            }
        }
    });
});

function openDeleteTaskModal(taskId) {
	$('#deleteTaskModal').data('taskId', taskId).modal('show');
}

$('#confirmDeleteTaskBtn').on('click', function() {
	const taskIdToDelete = $('#deleteTaskModal').data('taskId');

	if (taskIdToDelete !== undefined) {
		// AJAX request to delete task
		$.ajax({
			url: `/api/tasks/${taskIdToDelete}`,
			type: 'DELETE',
			headers: {
				'Authorization': `Bearer ${JwtStorage.getJwt()}`  // Attach JWT token
			},
			success: function() {
				$('#deleteTaskModal').modal('hide');  // Close the modal
				loadTasks();  // Reload tasks
			},
			error: function(xhr) {
				if (xhr.status === 404) {
					showErrorMessage('Task not found.');
				} else if (xhr.status === 403) {
					showErrorMessage('You do not have permission to delete this task.');
				} else {
					showErrorMessage('Error deleting task, please try again.');
				}
				$('#deleteTaskModal').modal('hide');  // Close the modal on error
			}
		});
	} else {
		showErrorMessage('No task selected for deletion.');
	}
});


function populateTaskChart(tasks) {
    const completedTasks = tasks.filter(task => task.status === "DONE").length;
    const uncompletedTasks = tasks.filter(task => task.status === "TODO" || task.status === "IN_PROGRESS").length;

    const chartContainer = document.getElementById("taskChart").parentElement;
    
    if (tasks.length === 0) {
        chartContainer.innerHTML = "<p class='no-tasks-msg'>No tasks yet. Start by adding your first task!</p>";
        return;
    } else {
        if (!document.getElementById("taskChart")) {
            chartContainer.innerHTML = '<canvas id="taskChart"></canvas>';
        }
    }

    const ctx = document.getElementById("taskChart").getContext("2d");

    // Destroy previous chart instance if it exists
    if (window.taskChartInstance) {
        window.taskChartInstance.destroy();
    }

    window.taskChartInstance = new Chart(ctx, {
        type: "pie",
        data: {
            labels: ["Completed", "Uncompleted"],
            datasets: [{
                data: [completedTasks, uncompletedTasks],
                backgroundColor: ["#2ecc71", "#f1c40f"],
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: "bottom" }
            }
        }
    });
}
