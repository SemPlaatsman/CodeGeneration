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

    Scenario: Successfully get all transactions
    Given the api is running
    When a request is to GET "/transactions"
    Then the response should be 200
    And should contain a List of transaction objects

    Example: Successfully get all transactions
    | id | amount | balance |
    | 1  | 20     | 30      |
    | 2  | 30     | 20      |


    Scenario: Successfully get a specific transaction by id
    Given a transaction with id 1 exists
    When a request is to GET a "/transactions/" by id 1
    Then the response should be 200 and contain the transaction with id 1

    Example: Successfully get a specific transaction by id
    | id | amount | balance |
    | 9999  | 20     | 30      |


    Scenario: Try to get a non-existent transaction by id
    Given no "/transactions/" with id 9999 exists
    When a request is to GET a "/transactions/" by bad id 9999
    Then the error response should be 404

    Example: Try to get a non-existent transaction by id
    | id | amount | balance |
    | 9999  | 20     | 30      |

