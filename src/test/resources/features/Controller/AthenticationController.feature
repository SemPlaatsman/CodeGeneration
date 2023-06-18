Feature: Authentication

  Scenario: Register a new user with valid request
    Given the API is running
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "testUser",
        "password": "testPassword",
        "firstName": "testFirstName",
        "lastName": "testLastName",
        "email": "testEmail",
        "phoneNumber": "testPhoneNumber",
        "birthDate": "testBirthDate",
      }
      """
    Then the response status should be 201
    And the response should contain an authentication token

  Scenario: Try to register a new user with an invalid request
    Given the API is running
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "",
        "password": "",
        "firstName": "",
        "lastName": ""
        "email": "",
        "phoneNumber": "",
        "birthDate": "",
      }
      """
    Then the response status should be 400

  Scenario: Register a new user with an already existing username or email
    Given the API is running
    And a user with username "testUser" and password "testPassword" already exists
    When I send a POST request to "/authenticate/register" with:
      """
      {
        "username": "testUser",
        "password": "testPassword",
        "firstName": "testFirstName",
        "lastName": "testLastName",
        "email": "testEmail",
        "phoneNumber": "testPhoneNumber",
        "birthDate": "testBirthDate",
      }
      """
    Then the response status should be 400

# Steps made
  Scenario: Login with valid credentials
    Given the API is running
    And a user with username "testUser" and password "testPassword" exists
    When I send a POST request to login "/authenticate/login" with:
      """
      {
        "username": "testUser",
        "password": "testPassword"
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
        "username": "testUser",
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