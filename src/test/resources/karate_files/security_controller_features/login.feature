Feature: Log in
		
  Scenario: Login as Test Teamworker
    Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "TestTeamworker", password: "TestPassword1" }
    When method post
    Then status 200
    And match response.jwt == '#string'
		
  Scenario: Login as Test Project Manager
    Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "TestProjectManager", password: "TestPassword1" }
    When method post
    Then status 200
    And match response.jwt == '#string'
    