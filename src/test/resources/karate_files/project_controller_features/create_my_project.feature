Feature: Create Project as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Successfully create a new project (Only Project Manager)
    Given url baseUrl + '/api/projects'
    * header Authorization = 'Bearer ' + token
    And request { projectName: 'Test Project' }
    When method post
    Then status 200
    And match response == '2'