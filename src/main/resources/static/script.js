document.addEventListener("DOMContentLoaded", function () {
    fetch("content/landingPage.html")
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("dynamic-container").innerHTML = html;
        })
        .catch(error => {
            console.error("Error loading login.html:", error);
        });
});

const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});

//in pomo and main aim to achieve - esp for guest
