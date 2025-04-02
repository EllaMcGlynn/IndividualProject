Feature: Update Task

 Background: Import Login Helper method and login as Teamworker
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestTeamworker', 'TestPassword1')

  Scenario: Update Task as Teamworker
  	Given url baseUrl + '/api/tasks/update/1'
  	* header Authorization = 'Bearer ' + token
    And request 
      """
      {
        "taskName": "New Task",
        "projectId": 1,
        "status": "IN_PROGRESS"
      }
      """
    When method PUT
    Then status 200
    And match response == 
    """
    {
    	id: 1, 
    	taskName: 'New Task', 
    	projectName: "TestProject1", 
    	assignedUser: 'TestTeamworker', 
   		status: 'IN_PROGRESS'
   	}
   	"""
   	
