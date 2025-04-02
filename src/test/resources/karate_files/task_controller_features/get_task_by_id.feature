Feature: Create Project as Project Manager

  Background: Import Login Helper method and login as Teamworker
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestTeamworker', 'TestPassword1')

  Scenario: Create a new Task
  	Given url baseUrl + '/api/tasks/1'
  	* header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    And match response == 
    """
    {
    	id: 1, 
    	taskName: 'TestTask1', 
    	projectName: null, 
    	assignedUser: 'TestTeamworker', 
   		status: 'TODO'
   	}
   	"""