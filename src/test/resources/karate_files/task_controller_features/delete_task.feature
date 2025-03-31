Feature: Delete Task

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_teamworker.feature')
    * def token = loginResponse.response.jwt

  Scenario: Delete Task
  	Given url baseUrl + '/api/tasks/1'
  	* header Authorization = 'Bearer ' + token
    When method DELETE
    Then status 204