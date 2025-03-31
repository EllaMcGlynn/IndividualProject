Feature: Log in as Teamworker
		
  Scenario: Request JWT from controller endpoint
    Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testProjectManager", password: "Password1" }
    When method post
    Then status 200
    * def token = response.token
