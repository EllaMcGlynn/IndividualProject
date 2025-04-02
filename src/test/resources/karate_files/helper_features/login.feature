Feature: Authentication API

  Scenario: Login and Get Token
    * def credentials = { username: '#(username)', password: '#(password)' }
    Given url baseUrl + '/api/auth/login'
    And request credentials
    When method post
    Then status 200