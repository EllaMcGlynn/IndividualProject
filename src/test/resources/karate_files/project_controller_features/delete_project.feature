Feature: Delete Project as Project Manager

	Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Successfully delete a project
    Given url baseUrl + '/api/projects/1'
    * header Authorization = 'Bearer ' + token
    When method delete
    Then status 204