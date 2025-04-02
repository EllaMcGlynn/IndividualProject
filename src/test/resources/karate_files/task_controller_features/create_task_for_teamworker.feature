Feature: Create Task for Worker as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Create Task for Worker as Project Manager
  	Given url baseUrl + '/api/tasks/manager/add'
  	* header Authorization = 'Bearer ' + token
    And request 
      """
      {
        "taskName": "New Task",
        "assignedUser": "TestTeamworker",
        "projectId": 1,
        "status": "IN_PROGRESS"
      }
      """
    When method POST
    Then status 200
    And match response ==
	    """
	    {
	    	taskId: 7,
	    	taskName: "New Task",
	    	assignedTo: "TestTeamworker",
	    	status: "IN_PROGRESS",
	    	projectName: "TestProject1"
	    }
	    """

    