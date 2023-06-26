package nl.inholland.codegeneration.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.lu.a;
import nl.inholland.codegeneration.models.User;

import org.springframework.beans.factory.annotation.Autowired;

public class SharedSteps {
    public RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ScenarioContext context;

    private ResponseEntity<String> response;
    // @Autowired
    private String authToken;

    @Given("the API is running")
    public void the_api_is_running() {

        authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjg3Nzc5NTg2LCJleHAiOjE2ODc4MTU1ODZ9.q9IzpS6-Iwe_gsEhi0rQDJ3eKWgJ_Pqvb9Hn64yetEk";
        String url = "http://localhost:8080/health";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + authToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);


        ResponseEntity<String> healthCheckResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (!"UP".equals(healthCheckResponse.getBody())) {
            throw new RuntimeException("API is not running");
        }
    }

    @When("I send a POST request to {string} with:")
    public void i_send_a_post_request_to_with(String path, String body) {
        String url = "http://localhost:8080" + path;
        HttpEntity<String> request = new HttpEntity<>(body);
        response = restTemplate.postForEntity(url, request, String.class);
        context.setResponse(response);
    }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer statusCode) {
    //     assertEquals(statusCode, context.getResponse().getStatusCodeValue());
    // }

    @Then("the response should contain an authentication token")
    public void the_response_should_contain_an_authentication_token() {
        assertTrue(context.getResponse().getBody().contains("token"));
    }

    @Then("the error response should be {int}")
    public void the_error_response_should_be(Integer int1) {
        assertEquals(context.getResponse().getStatusCode().value(), int1.intValue());
    }

    // @Given("logged in as {string}")
    //     public void logged_in_as(String role){
    //         try {
    //             if(!role.equals("EMPLOYEE")){
    //                 String username = "johndoe";
    //                 String password = "john123";
    //                 String url = "http://localhost:8080/authenticate/login";
    
    //                 ObjectMapper mapper = new ObjectMapper();
    
    //                 // Create a Map or a POJO to hold your post data
    //                 Map<String, String> params = new HashMap<>();
    //                 params.put("username", username);
    //                 params.put("password", password);
            
    //                 // Convert the map to JSON string
    //                 String jsonRequest = mapper.writeValueAsString(params);
            
    //                 // Set headers
    //                 HttpHeaders headers = new HttpHeaders();
    //                 headers.setContentType(MediaType.APPLICATION_JSON);
            
    //                 // Create the request entity
    //                 HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);
                    
    //                 ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    //                 authToken = response.getHeaders().get("Authorization").get(0);
    //             }


    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }


    // }
}
