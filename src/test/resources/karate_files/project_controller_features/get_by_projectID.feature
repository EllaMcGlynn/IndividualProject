Feature: Get a Project as Project Manager

  Background:
    * def loginResponse = callonce read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt
  
  Scenario: Retrieve list of projects successfully
    Given url baseUrl + '/api/projects/1'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
