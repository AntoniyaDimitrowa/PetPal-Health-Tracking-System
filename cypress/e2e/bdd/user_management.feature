Feature: User management

  Scenario: a logged in user can update their basic profile
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued
    When the user updates their name to "Updated BDD User" and address to "Antwerp, Belgium"
    Then the basic profile update should return status 204
    And the profile should contain the updated name and address

  Scenario: a logged in user can change their password
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued
    When the user changes their password to "NewPass1!A"
    Then the secure profile update should return status 204
    And the user can log in with the new password
