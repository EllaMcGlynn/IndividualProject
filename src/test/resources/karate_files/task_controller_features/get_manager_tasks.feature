Feature: Get Manager Tasks

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt

  Scenario: Get Manager Tasks
  	Given url baseUrl + '/api/tasks/manager/list'
  	* header Authorization = 'Bearer ' + token
    When method GET
    Then status 200