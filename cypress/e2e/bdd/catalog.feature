Feature: Catalog browsing

  Background:
    Given a registered user exists
    When the user logs in with valid credentials
    Then the system should return status 201
    And an access token should be issued

  Scenario: an authenticated owner can browse the seeded catalog
    When the user requests the breeds catalog
    Then the breed catalog should include the seeded breeds
    When the user requests the Golden Retriever breed details
    Then the Golden Retriever breed details should show the expected normal mood
    When the user requests the vaccinations catalog
    Then the vaccination catalog should include the seeded vaccinations
