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
	
	
	$("#login-btn").off("click").on("click", function () {
		let username = $("#username-sign-in").val();
		let password = $("#password-sign-in").val();
		
		$.ajax({
	        url: `/api/auth/login`,
	        method: "POST",
			headers: {
				"contentType": "application/json"
			},
	        contentType: "application/json",
	        data: JSON.stringify({username, password}),
	        success: function (response) {
				console.log("Jwt: " + response.jwt);
				JwtStorage.saveJwt(response.jwt);
			},
	        error: function () {
				console.log("Invalid username or password");
			}
	    });
	});
	
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
			},
	        error: function () {
				console.log("Error registering user");
			}
	    });
		
		
	});
});