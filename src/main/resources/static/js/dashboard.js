document.getElementById('openTaskModal').addEventListener('click', function() {
    var myModal = new bootstrap.Modal(document.getElementById('newTaskModal'));
    myModal.show();
});



$("#newTaskModal form").on("submit", function(event) {
	event.preventDefault();

	// Get values from the form fields
	const taskName = $("#taskName").val();
	const taskProject = $("#taskProject").val();
	const taskStatus = $("#taskStatus").val();

	// Create the task object
	const taskCreateRequest = {
		name: taskName,
		projectId: taskProject || null, // Use null if empty
		status: taskStatus
	};

	$.ajax({
		url: "api/tasks/add",
		type: "POST",
		headers: { Authorization: `Bearer ${JwtStorage.getJwt()}` },
		contentType: "application/json",
		data: JSON.stringify(taskCreateRequest),
		success: function(response) {
			console.log("Task created:", response);

			// Close the modal
			$("#newTaskModal").modal("hide");

			// Reset the form fields
			$("#newTaskModal form")[0].reset();

			// Optionally, update the task table dynamically
			addTaskToTable(response);
		},
		error: function(xhr, status, error) {
			console.error("Error creating task:", error);
			alert("Error creating task, please try again.");
		}
	});
});

// Function to add a new task to the table
function addTaskToTable(task) {
    $("#taskTable tbody").append(`
        <tr data-task-id="${task.id}">
            <td>${task.taskName}</td>
            <td>${task.projectName || "N/A"}</td>
            <td>${task.status}</td>
            <td>
                <button class="btn btn-outline-warning" data-bs-toggle="modal" data-bs-target="#editTaskModal" onclick="openEditModal(${task.id})">Edit</button>
                <button class="btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteTaskModal" onclick="openDeleteModal(${task.id})">Delete</button>
            </td>
        </tr>
    `);
}



