Feature: Vaccination tracking

  Background:
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued

  Scenario: an owner can add a vaccination record and see it in the pet details
    When the user creates a pet named "Vaccination Pet" with breed "Beagle"
    Then the pet creation should return status 201
    When the user adds a vaccination record for the pet
    Then the vaccination record creation should return status 201
    And the pet details should include the new vaccination record
