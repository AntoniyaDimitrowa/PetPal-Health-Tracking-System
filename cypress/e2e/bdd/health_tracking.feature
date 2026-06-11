Feature: Health tracking

  Background:
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued

  Scenario: an owner can record health data and inspect statistics
    When the user creates a pet named "Health Pet" with breed "Golden Retriever"
    Then the pet creation should return status 201
    When the user adds a health record for the pet
    Then the health record creation should return status 201
    And the health record should appear in the pet health history
    And the pet statistics should include the new record

  Scenario: one user cannot read another user's health records
    Given two registered users exist
    When the second user creates a pet named "Private Health Pet" with breed "Golden Retriever"
    Then the pet creation should return status 201
    When the second user adds a health record to that pet
    Then the health record creation should return status 201
    When the first user requests the second user's health records
    Then health record access should be rejected with status 403
