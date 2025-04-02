Feature: Registration
 	
 	Scenario: Register as a Project Manager
    Given url baseUrl + '/api/auth/register'
    And request
      """
      {
        "username": "project-manager-register1",
        "password": "Password1",
        "role": "ROLE_PROJECT_MANAGER"
      }
      """
    When method post
    Then status 201

 	Scenario: Register as a Teamworker
    Given url baseUrl + '/api/auth/register'
    And request
      """
      {
        "username": "teamworker-register1",
        "password": "Password1",
        "role": "ROLE_TEAMWORKER"
      }
      """
    When method post
    Then status 201
    
    