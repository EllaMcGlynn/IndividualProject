$(document).ready(function() { 
  // Menu Toggle  
  let toggle = document.querySelector('.toggle'); 
  let navigation = document.querySelector('.navigation'); 
  let main = document.querySelector('.main'); 

  toggle.onclick = function() { 
    navigation.classList.toggle('active'); 
    main.classList.toggle('active'); 
  };

  // Add Hovered Class in Selected List Item 
  let list = document.querySelectorAll('.navigation li'); 
  function activeLink() { 
    list.forEach((item) => item.classList.remove('hovered')); 
    this.classList.add('hovered'); 
  } 
  list.forEach((item) => item.addEventListener('mouseover', activeLink)); 

  // Retrieve the user role stored during login
  let userRole = localStorage.getItem("userRole");

  if(userRole === "PROJECT_MANAGER") {
    // Load the manager dashboard and hide teamworker links
    $("#loaded-content").load("components/managerHome.html");
    $("#dashboard-link").hide();  // Hide teamworker dashboard link

    // Bind click for manager dashboard link (if needed)
    $("#dashboardManager-link").on("click", function(e) { 
      e.preventDefault(); 
      $("#loaded-content").load("components/managerHome.html");
    });
  } else if(userRole === "TEAMWORKER") {
    // For other roles (e.g., teamworker), load the teamworker dashboard and hide manager links
    $("#loaded-content").load("components/teamworkerHome.html");
    $("#dashboardManager-link").hide(); // Hide manager dashboard link

    // Bind click for teamworker dashboard link (if needed)
    $("#dashboard-link").on("click", function(e) { 
      e.preventDefault(); 
      $("#loaded-content").load("components/teamworkerHome.html");
    });
  }

  // Bind click for pomodoro
  $("#pomodoro-link").on("click", function(e) { 
    e.preventDefault();
    $("#loaded-content").load("components/pomodoro.html", function() { 
      console.log("Pomodoro content loaded successfully"); 
    });
  }); 
});
