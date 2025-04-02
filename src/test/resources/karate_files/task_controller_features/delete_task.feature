Feature: Delete Task

    Background: Import Login Helper method and login as Teamworker
    * callonce read('classpath:karate_files/helper_features/login_helper.feature')
    * def token = login('TestTeamworker', 'TestPassword1')

  Scenario: Delete Task
  	Given url baseUrl + '/api/tasks/1'
  	* header Authorization = 'Bearer ' + token
    When method DELETE
    Then status 204
