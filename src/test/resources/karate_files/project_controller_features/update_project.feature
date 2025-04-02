Feature: Update Project as Project Manager

  Background: Import Login Helper method and login as Project Manager
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestProjectManager', 'TestPassword1')

  Scenario: Successfully update a project
    Given url baseUrl + '/api/projects/update/1'
    * header Authorization = 'Bearer ' + token
    And request 
    """
    { 
    	projectName: 'Updated Project Name'
    }
    """
    When method put
    Then status 200
    And match response.projectId == 1
    And match response.projectName == 'Updated Project Name'