Feature: Register as Project Manager
 	
 	Scenario: Create a Project Manager User
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
    Then assert responseStatus == 201