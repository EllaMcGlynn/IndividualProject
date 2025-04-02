Feature: Helper Functions

  Scenario: Login Function
    * def login = 
    """
    function(username, password) {
    	return karate.callSingle(
    		"classpath:karate_files/helper_features/login.feature", 
    		{username: username, password: password}
    	).response.jwt;
    }
    """