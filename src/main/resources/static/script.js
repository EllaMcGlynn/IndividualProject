$(document).ready(function() {
    checkAuthentication();

    function checkAuthentication() {
        const token = JwtStorage.getJwt();

        if (!token) {
            loadLoginPage();
        } else {
            loadHomePage();
        }
    }

    // Load the login page
    function loadLoginPage() {
        $("#dynamic-container").load("components/login.html", function(response, status, xhr) {
            if (status === "error") {
                console.error("Error loading login page:", xhr.status, xhr.statusText);
            } else {
                initializeLoginEventListeners();
            }
        });
    }

    // Load the homepage content
    function loadHomePage() {
        $("#dynamic-container").load("components/homepage.html", function(response, status, xhr) {
            if (status === "error") {
                console.error("Error loading homepage:", xhr.status, xhr.statusText);
            }

            // Initialize event listeners after loading homepage
            initializeHomePageEventListeners();
            loadUsername();
        });
    }

    // Show success/error message to the user
    function showMessage(message, type) {
        const messageContainer = $("#message-container");

        messageContainer.html(`
            <div class="alert ${type}">
                <span class="message">${message}</span>
            </div>
        `);

        // Show the message container with fade-in effect
        messageContainer.addClass('show');

        // Hide the message after 5 seconds
        setTimeout(function() {
            messageContainer.removeClass('show'); // Fade out the message
        }, 3000);
    }

    // Initialize event listeners for login page
    function initializeLoginEventListeners() {
        const sign_in_btn = $("#sign-in-btn");
        const sign_up_btn = $("#sign-up-btn");
        const container = $(".container");

        if (sign_in_btn.length && sign_up_btn.length && container.length) {
            sign_up_btn.on("click", function() {
                container.addClass("sign-up-mode");
            });

            sign_in_btn.on("click", function() {
                container.removeClass("sign-up-mode");
            });

            // Login button click event
            $("#login-btn").on("click", function() {
                let username = $("#username-sign-in").val();
                let password = $("#password-sign-in").val();

                $.ajax({
                    url: `/api/auth/login`,
                    method: "POST",
                    headers: {
                        "contentType": "application/json"
                    },
                    contentType: "application/json",
                    data: JSON.stringify({ username, password }),
                    success: function(response) {
                        console.log("Jwt: " + response.jwt);
						JwtStorage.saveJwt(response.jwt);
                        localStorage.setItem("username", username);

                        loadHomePage();
                    },
                    error: function() {
                        console.log("Invalid username or password");
                        showMessage("Error during login. Please try again.", "error");
                    }
                });
            });
        }
    }

    // Load the username into the user icon
    function loadUsername() {
        const username = localStorage.getItem('username') || 'Guest';
        $('#username').text(username);
    }


    function initializeHomePageEventListeners() {
        $("#logout-link").on("click", function(e) {
            e.preventDefault();
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("username");

            loadLoginPage(); 
        });
    }
});
