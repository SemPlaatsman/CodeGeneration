package nl.inholland.codegeneration.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.lu.an;
import io.cucumber.java.en.Then;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;

import java.util.List;

import org.apiguardian.api.API;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import io.cucumber.java.en.Given;

public class UserSteps {

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserResponseDTO[]> response = restTemplate.getForEntity(url, UserResponseDTO[].class);
    // Scenario: Fetching All Users with specific filter and limit

    @Given("there are users in the system and I have 'EMPLOYEE' authority.")
    public void there_are_users_in_the_system_and_I_have_EMPLOYEE_authority() {

    }
    @When("I send a GET request to {string} endpoint with the filter set as {string} and limit set as {int}.")
    public void i_send_a_GET_request_to_users_endpoint_with_the_filter_set_as_firstName_John_and_limit_set_as_20(
            String path, String filter, Integer limit) {
        String url = "http://localhost:8080/api" + path + "?filter=" + filter + "&limit=" + limit;
        this.response = restTemplate.getForEntity(url, UserResponseDTO[].class);
    }
    @Then("I should receive a 200 OK response with a list of UserResponseDTOs of users with first name 'John' and the list should not contain more than 20 users.")
    public void i_should_receive_a_200_OK_response_with_a_list_of_UserResponseDTOs_of_users_with_first_name_John_and_the_list_should_not_contain_more_than_20_users() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length <= 20);
        for (UserResponseDTO user : response.getBody()) {
            assertEquals("John", user.getFirstName());
        }
    }

    //Scenario: Fetching All Users with Invalid Filter Format
    @Given("there are users in the system and I have 'EMPLOYEE' authority.")
    public void there_are_users_in_the_system_and_I_have_EMPLOYEE_authority() {

    }
    @When("I send a GET request to {string} endpoint with the filter set as {string} (invalid format).")
    public void i_send_a_GET_request_to_users_endpoint_with_the_filter_set_as_firstName_John_invalid_format(String path,
            String filter) {
        String url = "http://localhost:8080/api" + path + "?filter=" + filter;
        this.response = restTemplate.getForEntity(url, UserResponseDTO[].class);
    }
    @Then("I should receive a 400 Bad Request response.")
    public void i_should_receive_a_400_Bad_Request_response() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Scenario: Fetching a Specific User
    @Given("there are users in the system and I have 'EMPLOYEE' or 'CUSTOMER' authority.")
    public void there_are_users_in_the_system_and_I_have_EMPLOYEE_or_CUSTOMER_authority() {

    }
    @When("I send a GET request to {string} endpoint with a valid user ID.")
    public void i_send_a_GET_request_to_users_endpoint_with_a_valid_user_ID(String path) {
        String url = "http://localhost:8080/api" + path;
        this.response = restTemplate.getForEntity(url, UserResponseDTO[].class);
    }
    @Then("I should receive a 200 OK response with UserResponseDTO of the user with the corresponding ID.")
    public void i_should_receive_a_200_OK_response_with_UserResponseDTO_of_the_user_with_the_corresponding_ID() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (UserResponseDTO user : response.getBody()) {
            assertEquals("John", user.getFirstName());
        }
    }

     // Scenario: Fetching User by Invalid ID
        @Given("there are users in the system and I have 'EMPLOYEE' authority.")
        public void there_are_users_in_the_system_and_I_have_EMPLOYEE_authority() {

        }
        @When("I send a GET request to {string} endpoint with an ID that does not exist in the system.")
        public void i_send_a_GET_request_to_users_endpoint_with_an_ID_that_does_not_exist_in_the_system(String path) {
            String url = "http://localhost:8080/api" + path;
            this.response = restTemplate.getForEntity(url, UserResponseDTO[].class);
        }
        @Then("I should receive a 404 Not Found response.")
        public void i_should_receive_a_404_Not_Found_response() {
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }



}
