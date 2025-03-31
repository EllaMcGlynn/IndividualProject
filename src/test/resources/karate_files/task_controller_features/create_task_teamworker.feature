Feature: Create Project as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_teamworker.feature')
    * def token = loginResponse.response.jwt

  Scenario: Create a new Task
  	Given url baseUrl + '/api/tasks/add'
  	* header Authorization = 'Bearer ' + token
    And request 
      """
      {
        "name": "New Task",
        "projectId": 1,
        "status": "IN_PROGRESS"
      }
      """
    When method POST
    Then status 201
    And match response.name == "New Task"
    And match response.status == "IN_PROGRESS"
    * def taskId = response.taskId