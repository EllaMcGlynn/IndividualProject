Feature: Create Task for Worker as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt

  Scenario: Create Task for Worker as Project Manager
  	Given url baseUrl + '/api/tasks/manager/add'
  	* header Authorization = 'Bearer ' + token
    And request 
      """
      {
        "taskName": "New Task",
        "assignedUser": "ella",
        "projectId": 1,
        "status": "IN_PROGRESS"
      }
      """
    When method POST
    Then status 200