Feature: Get users as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt

  Scenario: Successfully delete a project
    Given url baseUrl + '/api/users'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200