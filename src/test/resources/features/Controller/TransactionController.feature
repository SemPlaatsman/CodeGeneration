Feature: Transactions
  As a owner of a bank account
  I want to transfer money to another bank account
  So that I can pay for my groceries

  Scenario: Transfer money to another bank account
    Given I have 50 on my bank account
    When I transfer 20 to the other bank account
    Then I should have 30 on my bank account

    Example: Transfer money to another bank account
      | amount | balance |
      | 20     | 30      |
      | 30     | 20      |
      | 40     | 10      |
      | 50     | 0       |

#Feature: Transactions
    Scenario: Successfully get all transactions
    Given the api is running
    When a request is to GET transactions
    Then the response should be 200
    And should contain a List of transaction objects


#Feature: Transactions

    Scenario: Successfully get a specific transaction by id
    Given a transaction with id 1 exists
    When a request is to GET a transaction by id 1
    Then the response should be 200 and contain the transaction with id 1

    Scenario: Try to get a non-existent transaction by id
    Given no transaction with id 9999 exists
    When a request is to GET a transaction by bad id 9999
    Then the error response should be 404
