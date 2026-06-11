Feature: Authentication

  Scenario: a registered user can log in and read their own profile
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued
    When the profile is requested for the logged in user
    Then the profile should match the registered user

  Scenario: invalid credentials are rejected
    Given a registered user with invalid login credentials
    When the user attempts to log in with invalid credentials
    Then invalid credentials should be rejected with status 404
