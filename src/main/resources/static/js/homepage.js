// Menu Toggle  
let toggle = document.querySelector('.toggle'); 
let navigation = document.querySelector('.navigation'); 
let main = document.querySelector('.main'); 

toggle.onclick = function() { 
	navigation.classList.toggle('active'); 
	main.classList.toggle('active'); 
} 

 

// Add Hovered Class in Selected List Item 
let list = document.querySelectorAll('.navigation li'); 
function activeLink() { 
	list.forEach((item) => item.classList.remove('hovered')); 
	this.classList.add('hovered'); 
} 

 

list.forEach((item) => item.addEventListener('mouseover', activeLink)); 


// Dynamic Content Loading Based on Menu Click 
$(document).ready(function() { 
	$("#dynamic-content").load("content/dashboard.html"); // Load dashboard by default 
	$("#dashboard-link").on("click", function(e) { 
		e.preventDefault(); 
		$("#dynamic-content").load("content/dashboard.html");
	}); 
	
	$("#kanban-link").on("click", function(e) { 
		e.preventDefault();
		$("#dynamic-content").load("content/kanban.html"); 
	});
	
	$("#pomodoro-link").on("click", function(e) { 
		e.preventDefault();
		$("#dynamic-content").load("content/pomodoro.html", function() { 
			console.log("Pomodoro content loaded successfully"); 
			initializePomodoroTimer(); // Initialize the Pomodoro timer only after loading 
		}); 
	}); 
}); 



// Initialize Pomodoro Timer Logic 
function initializePomodoroTimer() { 
	let workDuration = 25 * 60; // Default: 25 minutes in seconds 
	let breakDuration = 5 * 60; // Default: 5 minutes in seconds 
	let timeRemaining = workDuration; 
	let timerInterval; 
	let isWorkSession = true; 
	
	const minutesElement = document.getElementById("minutes"); 
	const secondsElement = document.getElementById("seconds"); 
	const statusElement = document.getElementById("status");  
	
	const startButton = document.getElementById("start-button"); 
	const stopButton = document.getElementById("stop-button"); 
	const resetButton = document.getElementById("reset-button");  
	
	const workTimeInput = document.getElementById("work-time"); 
	const breakTimeInput = document.getElementById("break-time"); 
	
	function formatTime(seconds) { 
		const minutes = Math.floor(seconds / 60); 
		const remainingSeconds = seconds % 60; 
		return `${minutes < 10 ? "0" : ""}${minutes}:${remainingSeconds < 10 ? "0" : ""}${remainingSeconds}`; 
	}
	
	function updateDisplay() { 
		minutesElement.textContent = formatTime(timeRemaining).split(":")[0]; 
		secondsElement.textContent = formatTime(timeRemaining).split(":")[1]; 
	}
	
	function startTimer() { 
		// Get the user-defined work and break durations 
		workDuration = parseInt(workTimeInput.value) * 60; // Convert minutes to seconds 
		breakDuration = parseInt(breakTimeInput.value) * 60; // Convert minutes to seconds 
		
		if (timerInterval) return; // If already running, prevent starting a new timer 
		
		timerInterval = setInterval(function () { 
			timeRemaining--; 
			
			updateDisplay(); 
			
			if (timeRemaining <= 0) { 
				clearInterval(timerInterval); 
				timerInterval = null; 	 
				
				if (isWorkSession) { 
					// Switch to break session 
					timeRemaining = breakDuration; 
					statusElement.textContent = "Break Time"; 
					statusElement.style.color = "#f44336"; // Break session color
				} else { 
					// Switch back to work session 
					timeRemaining = workDuration; 
					statusElement.textContent = "Work Session"; 
					statusElement.style.color = "#4CAF50"; // Work session color 
				} 
				
				isWorkSession = !isWorkSession; // Toggle session 
				
			} 
				
		}, 1000);
	} 
	
	
	function stopTimer() { 
		clearInterval(timerInterval); 
		timerInterval = null; 
	} 
	
	 
	
	function resetTimer() { 
		clearInterval(timerInterval); 
		
		timerInterval = null; 
		timeRemaining = workDuration; 
		
		updateDisplay(); 
		
		statusElement.textContent = "Work Session"; 
		statusElement.style.color = "#4CAF50"; // Work session color 
	} 
	
	startButton.addEventListener("click", startTimer); 
	stopButton.addEventListener("click", stopTimer); 
	resetButton.addEventListener("click", resetTimer); 
		
	updateDisplay(); // Initialize the display 
	
} 
	
	 
	
// Load other sections (Dashboard, Kanban) as needed 	
function loadDashboardContent() { 
	const dashboardContent = `<div class="dashboard"> 
							  	<h2>Welcome to the Dashboard</h2> 
								<p>Your dashboard content goes here.</p> 
							  </div>`;
	dynamicContainer.innerHTML = dashboardContent; 
} 
	
function loadKanbanContent() { 	
	const kanbanContent = `<div class="kanban"> 
								<h2>Kanban Board</h2> 
								<p>Manage your tasks with the Kanban board!</p> 
						   </div>`; 
	dynamicContainer.innerHTML = kanbanContent; 
} 
