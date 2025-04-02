Feature: Get Projects as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')
  
  Scenario: Retrieve list of projects successfully
    Given url baseUrl + '/api/projects'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response[0].projectId == 1
    And match response[0].projectName == 'TestProject1'
