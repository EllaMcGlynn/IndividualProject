Feature: Create Task as a Teamworker

  Background: Import Login Helper method and login as Teamworker
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestTeamworker', 'TestPassword1')

  Scenario: Create a new Task and assign to project with id 1
  	* def projectId = 1
  	Given url baseUrl + '/api/tasks/add'
  	* header Authorization = 'Bearer ' + token
    And request 
      """
      {
        "name": "New Task",
        "projectId": #(projectId),
        "status": "IN_PROGRESS"
      }
      """
    When method POST
    Then status 201
    And match response.id == 7
    And match response.taskName == 'New Task'
    And match response.projectName == 'TestProject1'
    And match response.assignedUser == 'TestTeamworker'
    And match response.status == 'IN_PROGRESS'