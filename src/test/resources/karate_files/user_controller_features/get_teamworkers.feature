Feature: Get teamworkers as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Get All Teamworkers
    Given url baseUrl + '/api/users'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response == '#[1]'
    And match each response == 
	    """
	    {
	    	id: '#number',
	    	username: '#string'
	    }
	    """
    
    
    