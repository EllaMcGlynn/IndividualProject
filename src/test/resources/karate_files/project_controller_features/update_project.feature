Feature: Update Project as Project Manager

  Background:
    * def loginResponse = call read('classpath:karate_files/security_controller_features/login_as_project_manager.feature')
    * def token = loginResponse.response.jwt

  Scenario: Successfully update a project
    Given url baseUrl + '/api/projects/update/1'
    * header Authorization = 'Bearer ' + token
    And request { projectName: 'Updated Project Name'}
    When method put
    Then status 200