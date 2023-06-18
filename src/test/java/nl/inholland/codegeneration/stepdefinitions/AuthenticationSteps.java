package nl.inholland.codegeneration.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.junit.platform.commons.function.Try;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.lu.an;
import nl.inholland.codegeneration.models.User;

public class AuthenticationSteps {
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> response;

    // Scenario: Register a new user with valid request
    @Given("the API is running")
    public void the_api_is_running() {
        String url = "http://localhost:8080/health";
        ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url, String.class);
        if (!"UP".equals(healthCheckResponse.getBody())) {
            throw new RuntimeException("API is not running");
        }
    }

    @When("I send a POST request to {string} with:")
    public void i_send_a_post_request_to_with(String string, User user) {
        response = restTemplate.postForEntity("http://localhost:8080/authenticate/register", user, String.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @And("the response should contain an authentication token")
    public void the_response_should_contain_an_authentication_token() {
        response.getBody().contains("token");
    }

    // Scenario: Try to register a new user with an invalid request
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     String url = "http://localhost:8080/health";
    //     ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url, String.class);
    //     if (!"UP".equals(healthCheckResponse.getBody())) {
    //         throw new RuntimeException("API is not running");
    //     }
    // }

    // @When("I send a POST request to {string} with:")
    // public void i_send_a_post_request_to_with(String string, User user) {
    //     response = restTemplate.postForEntity("http://localhost:8080/authenticate/register", user, String.class);
    // }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer statusCode) {
    //     assertEquals(statusCode, response.getStatusCodeValue());
    // }

    // Scenario: Register a new user with an already existing username or email
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     String url = "http://localhost:8080/health";
    //     ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url, String.class);
    //     if (!"UP".equals(healthCheckResponse.getBody())) {
    //         throw new RuntimeException("API is not running");
    //     }
    // }

    @And("a user with username {string} and password {string} already exists")
    public void a_user_with_username_and_password_already_exists(String username, String password) {
        Object user = new User();
        ((User) user).setUsername(username);
        ((User) user).setPassword(password);
        String url = "http://localhost:8080/authenticate/login" + user;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().value() != 200) {
            throw new RuntimeException("User could not be created");
        }
    }

    // @When("I send a POST request to {string} with:")
    // public void i_send_a_post_request_to_with(String string, User user) {
    //     response = restTemplate.postForEntity("http://localhost:8080/authenticate/register", user, String.class);
    // }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer statusCode) {
    //     assertEquals(statusCode, response.getStatusCodeValue());
    // }

    // Scenario: Login with valid credentials
    // @Given("the API is running")
    // public void the_api_is_running() {
    // String url = "http://localhost:8080/health";
    // ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url,
    // String.class);
    // if (!"UP".equals(healthCheckResponse.getBody())) {
    // throw new RuntimeException("API is not running");
    // }
    // }

    // @And("a user with username {string} and password {string} exists")
    // public void a_user_with_username_and_password_exists(String username, String password) {
    //     // Here you would typically interact with your test database to create a new
    //     // user
    //     Object user = new User();
    //     ((User) user).setUsername(username);
    //     ((User) user).setPassword(password);
    //     String url = "http://localhost:8080/authenticate/login" + user;
    //     ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    //     if (response.getStatusCode().value() != 200) {
    //         throw new RuntimeException("User could not be created");
    //     }
    // }

    // @When("I send a POST request to {string} with:")
    // public void i_send_a_post_request_to_with(String path, String body) {
    //     String url = "http://localhost:8080" + path;
    //     HttpEntity<String> request = new HttpEntity<>(body);
    //     this.response = restTemplate.postForEntity(url, request, String.class);
    // }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(int statusCode) {
    //     assertEquals(statusCode, response.getStatusCode().value());
    // }

    // @And("the response should contain an authentication token")
    // public void the_response_should_contain_an_authentication_token() {
    //     // Here you need to parse the response body and assert that it contains an
    //     // authentication token
    // }

    // Scenario: Login with invalid credentials
    // @Given("the API is running")
    // public void the_api_is_running() {
    // String url = "http://localhost:8080/health";
    // ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url,
    // String.class);
    // if (!"UP".equals(healthCheckResponse.getBody())) {
    // throw new RuntimeException("API is not running");
    // }
    // }

    // @When("I send a POST request to {string} with:")
    // public void i_send_a_post_request_to_with(String path, String body) {
    //     String url = "http://localhost:8080" + path;
    //     HttpEntity<String> request = new HttpEntity<>(body);
    //     this.response = restTemplate.postForEntity(url, request, String.class);
    // }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(int statusCode) {
    //     assertEquals(statusCode, response.getStatusCode().value());
    // }

    // Scenario: Login with non-existent user credentials
    // @Given("the API is running")
    // public void the_api_is_running() {
    // String url = "http://localhost:8080/health";
    // ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url,
    // String.class);
    // if (!"UP".equals(healthCheckResponse.getBody())) {
    // throw new RuntimeException("API is not running");
    // }
    // }

    @When("I send a POST request to login {string} with:")
    public void i_send_a_post_request_to_login_with(String path, String body) {
        String url = "http://localhost:8080" + path;
        HttpEntity<String> request = new HttpEntity<>(body);
        this.response = restTemplate.postForEntity(url, request, String.class);
    }

    @Then("the login response status should be {int}")
    public void the_login_response_status_should_be(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

}