Feature: Authorization

  Scenario: unauthenticated visitors cannot access protected resources
    When an anonymous user requests the breeds list
    Then anonymous access should be rejected with status 401

  Scenario: one user cannot read another user profile
    Given two registered users exist
    When the first user requests the second user's profile
    Then profile access should be rejected with status 403

  Scenario: one user cannot update another user's profile
    Given two registered users exist
    When the first user tries to update the second user's profile
    Then profile update should be rejected with status 403
