Feature: Register as Teamworker
 	
 	Scenario: Create a Teamworker
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
    Then assert responseStatus == 201