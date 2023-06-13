Feature: Get All Accounts
  Scenario: Successfully get all accounts
    Given the API is running
    When a request is made to GET /accounts
    Then the response status should be 200
    And the response body should contain a list of account objects


Feature: Get Account by IBAN
  Scenario: Successfully get an account by IBAN
    Given the API is running
    And there is an account with IBAN "123456789"
    When a request is made to GET /accounts/123456789
    Then the response status should be 200
    And the response body should contain the account object with IBAN "123456789"

  Scenario: Attempt to get a non-existent account by IBAN
    Given the API is running
    And there is no account with IBAN "987654321"
    When a request is made to GET /accounts/987654321
    Then the response status should be 404

Feature: Insert Account
  Scenario: Successfully insert an account
    Given the API is running
    And the request body contains valid account data
    When a request is made to POST /accounts
    Then the response status should be 201
    And the response body should contain the inserted account object

  Scenario: Attempt to insert an account with invalid data
    Given the API is running
    And the request body contains invalid account data
    When a request is made to POST /accounts
    Then the response status should be 400

Feature: Update Account
  Scenario: Successfully update an account
    Given the API is running
    And there is an account with IBAN "123456789"
    And the request body contains valid account data
    When a request is made to PUT /accounts/123456789
    Then the response status should be 200
    And the response body should contain the updated account object

  Scenario: Attempt to update a non-existent account
    Given the API is running
    And there is no account with IBAN "987654321"
    And the request body contains valid account data
    When a request is made to PUT /accounts/987654321
    Then the response status should be 404

Feature: Delete Account
  Scenario: Successfully delete an account
    Given the API is running
    And there is an account with IBAN "123456789"
    When a request is made to DELETE /accounts/123456789
    Then the response status should be 204

  Scenario: Attempt to delete a non-existent account
    Given the API is running
    And there is no account with IBAN "987654321"
    When a request is made to DELETE /accounts/987654321
    Then the response status should be 404
