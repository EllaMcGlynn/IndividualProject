Feature: Get Manager Tasks

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Get Manager Tasks
  	Given url baseUrl + '/api/tasks/manager/list'
  	* header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    And match response == '#[3]'
    And match each response == 
    """
    {
    	id: '#number', 
    	taskName: '#string', 
    	projectName: '#? _ == null || typeof _ == "string"', 
    	assignedUser: '#string', 
   		status: '#string'
   	}
   	"""