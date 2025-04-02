Feature: Get Tasks

  Background: Import Login Helper method and login as Teamworker
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestTeamworker', 'TestPassword1')

  Scenario: Get Tasks
  	Given url baseUrl + '/api/tasks/list'
  	* header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    And match response == '#[6]'
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