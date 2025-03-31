Feature: Create Project as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_teamworker.feature')
    * def token = loginResponse.response.jwt
    
    * def createTaskResponse = call read('classpath:karate_files/task_controller_features/create_task_teamworker.feature')
    * def taskId = createTaskResponse.response.id

  Scenario: Create a new Task
  	Given url baseUrl + '/api/tasks/update/' + taskId
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