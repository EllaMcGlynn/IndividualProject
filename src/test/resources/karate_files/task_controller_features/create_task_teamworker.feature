Feature: Create Project as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_teamworker.feature')
    * def token = loginResponse.response.jwt
    
    * def projectCreationResponse = call read('classpath:karate_files/project_controller_features/create_my_project.feature')
    * def projectId = projectCreationResponse.response

  Scenario: Create a new Task
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