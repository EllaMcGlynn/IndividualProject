$(document).ready(function () {
	const sign_in_btn = document.querySelector("#sign-in-btn");
	const sign_up_btn = document.querySelector("#sign-up-btn");
	const container = document.querySelector(".container");

	sign_up_btn.addEventListener("click", () => {
		container.classList.add("sign-up-mode");
	});

	sign_in_btn.addEventListener("click", () => {
		container.classList.remove("sign-up-mode");
	});
	
	// Toggle password visibility for sign-in
	    $("#toggle-sign-in").on("click", function () {
	        let passwordField = $("#password-sign-in");
	        let icon = $(this);

	        if (passwordField.attr("type") === "password") {
	            passwordField.attr("type", "text"); // Show the password
	            icon.removeClass("bi-lock-fill").addClass("bi-unlock-fill"); // Change to unlocked icon
	        } else {
	            passwordField.attr("type", "password"); // Hide the password
	            icon.removeClass("bi-unlock-fill").addClass("bi-lock-fill"); // Change to locked icon
	        }
	    });

	    // Toggle password visibility for sign-up
	    $("#toggle-sign-up").on("click", function () {
	        let passwordField = $("#password-sign-up");
	        let icon = $(this);

	        if (passwordField.attr("type") === "password") {
	            passwordField.attr("type", "text"); // Show the password
	            icon.removeClass("bi-lock-fill").addClass("bi-unlock-fill"); // Change to unlocked icon
	        } else {
	            passwordField.attr("type", "password"); // Hide the password
	            icon.removeClass("bi-unlock-fill").addClass("bi-lock-fill"); // Change to locked icon
	        }
	    });
		
		function showMessage(message, type) {
		       const messageContainer = $("#message-container");

		       // Add message and type class to message container
		       messageContainer.html(`
		           <div class="alert ${type}">
		               <span class="message">${message}</span>
		           </div>
		       `);

		       // Show the message container with fade-in effect
		       messageContainer.addClass('show');

		       // Hide the message after 5 seconds
		       setTimeout(function () {
		           messageContainer.removeClass('show'); // Fade out the message
		       }, 3000); // Remove the message after 5 seconds
		   }
	
	
	$("#signup-btn").off("click").on("click", function () {
		let username = $("#username-sign-up").val();
		let password = $("#password-sign-up").val();
		let role = $('#role-sign-up').val();
		
		$.ajax({
	        url: `/api/auth/register`,
	        method: "POST",
			headers: {
				"contentType": "application/json"
			},
	        contentType: "application/json",
	        data: JSON.stringify({username, password, role}),
	        success: function (response) {
				console.log("Jwt: " + response.jwt);
				JwtStorage.saveJwt(response.jwt);
				showMessage("Registration successful!", "success");
			},
	        error: function () {
				console.log("Error registering user");
				showMessage("Error during registration. Please try again.", "error");
			}
	    });	
	});
	
});