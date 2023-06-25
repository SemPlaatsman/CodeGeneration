package nl.inholland.codegeneration.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.codegeneration.models.User;

public class SharedSteps {
    public final RestTemplate restTemplate = new RestTemplate();
    public ResponseEntity<String> response;

    @Given("the API is running")
    public void the_api_is_running() {
        String url = "http://localhost:8080/health";
        ResponseEntity<String> healthCheckResponse = restTemplate.getForEntity(url, String.class);
        if (!"UP".equals(healthCheckResponse.getBody())) {
            throw new RuntimeException("API is not running");
        }
    }

    @When("I send a POST request to {string} with:")
    public void i_send_a_post_request_to_with(String path, String body) {
        String url = "http://localhost:8080" + path;
        HttpEntity<String> request = new HttpEntity<>(body);
        this.response = restTemplate.postForEntity(url, request, String.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @Then("the response should contain an authentication token")
    public void the_response_should_contain_an_authentication_token() {
        assertTrue(response.getBody().contains("token"));
    }

    @Then("the error response should be {int}")
    public void the_error_response_should_be(Integer int1) {
        assertEquals(response.getStatusCode().value(), int1.intValue());
    }
}
