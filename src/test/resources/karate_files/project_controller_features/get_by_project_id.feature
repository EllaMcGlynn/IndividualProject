Feature: Get a Project as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')
  
  Scenario: Retrieve list of projects successfully
    Given url baseUrl + '/api/projects/1'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response.projectId == 1
    And match response.projectName == 'TestProject1'
