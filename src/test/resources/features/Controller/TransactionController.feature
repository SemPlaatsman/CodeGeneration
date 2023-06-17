Feature: Transaction
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

