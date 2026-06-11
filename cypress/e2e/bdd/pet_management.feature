Feature: Pet management

  Background:
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued

  Scenario: an owner can create a pet and read it back
    When the user creates a pet named "Milo" with breed "Golden Retriever"
    Then the pet creation should return status 201
    And the pet should be retrievable with the expected details

  Scenario: one user cannot access another user's pet
    Given two registered users exist
    When the second user creates a pet named "Private Pet" with breed "Beagle"
    Then the pet creation should return status 201
    When the first user requests the second user's pet
    Then pet access should be rejected with status 403
