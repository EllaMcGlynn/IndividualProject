Feature: Get Tasks

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_teamworker.feature')
    * def token = loginResponse.response.jwt

  Scenario: Get Tasks
  	Given url baseUrl + '/api/tasks/list'
  	* header Authorization = 'Bearer ' + token
    When method GET
    Then status 200