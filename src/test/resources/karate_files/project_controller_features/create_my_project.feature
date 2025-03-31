Feature: Create Project as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt

  Scenario: Successfully create a new project
    Given url baseUrl + '/api/projects'
    * header Authorization = 'Bearer ' + token
    And request { projectName: 'Test Project' }
    When method post
    Then status 200