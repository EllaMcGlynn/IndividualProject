// Dynamic Content Loading Based on Menu Click 
$(document).ready(function() { 
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
	
	$("#loaded-content").load("components/dashboard.html"); 
	$("#dashboard-link").on("click", function(e) { 
		e.preventDefault(); 
		$("#loaded-content").load("components/dashboard.html");
	}); 
	
	
	$("#pomodoro-link").on("click", function(e) { 
		e.preventDefault();
		$("#loaded-content").load("components/pomodoro.html", function() { 
			console.log("Pomodoro content loaded successfully"); 
			initializePomodoroTimer(); 
		}); 
	}); 
}); 
