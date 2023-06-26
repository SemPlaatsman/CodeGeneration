Feature: Authentication

  #werkt
  Scenario: Register a new user with valid request
    Given the API is running
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "janedoe",
        "password": "jane123",
        "firstName": "jane",
        "lastName": "doe",
        "email": "jane@doe.com",
        "phoneNumber": "1234567890",
        "birthdate": "2000-07-09"
      }
      """
    Then the response status should be 201
    And the response should contain an authentication token

    

    #werkt
  Scenario: Try to register a new user with an invalid request
    Given the API is running
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "",
        "password": "",
        "firstName": "",
        "lastName": "",
        "email": "",
        "phoneNumber": "",
        "birthdate": ""
      }
      """
    Then the error response should be 400

    #werkt
  Scenario: Register a new user with an already existing username or email
    Given the API is running
    And a user with username "johndoe" and password "john123" already exists
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "johndoe",
        "password": "john123",
        "firstName": "testFirstName",
        "lastName": "testLastName",
        "email": "testEmail@example.com",
        "phoneNumber": "1234567890",
        "birthdate": "2000-01-01"
      }
      """
    Then the error response should be 400

# Steps made
  Scenario: Login with valid credentials
    Given the API is running
    And a user with username "testUser" and password "testPassword" exists
    When I send a POST request to login "/authenticate/login" with:
      """
      {
        "username": "johndoe",
        "password": "john123"
      }
      """
    Then the login response status should be 200
    And the response should contain an authentication token

# Steps made
  Scenario: Login with invalid credentials
    Given the API is running
    When I send a POST request to login "/authenticate/login" with:
      """
      {
        "username": "johndoe",
        "password": "wrongPassword"
      }
      """
    Then the login response status should be 401

# Steps made
  Scenario: Login with non-existent user credentials
    Given the API is running
    When I send a POST request to login "/authenticate/login" with:
      """
      {
        "username": "nonExistentUser",
        "password": "testPassword"
      }
      """
    Then the login response status should be 401