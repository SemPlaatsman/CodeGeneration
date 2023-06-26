package nl.inholland.codegeneration.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.lu.an;
import io.cucumber.java.en.Then;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;

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

public class AccountSteps {

    private Account account;
    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<Account[]> response;
    private ResponseEntity<Account> responseAccount;
    private AccountRequestDTO accountRequestDTO;

    //Scenario: Successfully get all accounts
    @When("a request is made to GET {string}")
    public void a_request_is_made_to_get_accounts(String path) {
        String url = "http://localhost:8080" + path;
        response = restTemplate.getForEntity(url, Account[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
        
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }
    @And("the response body should contain a list of account objects")
    public void the_response_body_should_contain_a_list_of_account_objects() {        
        assertTrue(response.getBody().length > 0);
    }    
    //Scenario: Successfully get an account by iban
    @And("there is an account with IBAN {string}")
    public void there_is_an_account_with_iban(String iban) {
        String url = "http://localhost:8080/api/accounts/" + iban;
        account = restTemplate.getForObject(url, Account.class);
        restTemplate.postForEntity(url, account, Account.class);
    }
    //Scenario: Successfully get an account by iban
    @And("the response body should contain the account object with IBAN {string}")
    public void the_response_body_should_contain_the_account_object_with_iban(String iban) {
        assertEquals(iban, account.getIban());
    }
    //Scenario: Attempt to get a non-existent account by IBAN
    @And("there is no account with IBAN {string}")
    public void there_is_no_account_with_iban(String iban) {
        String url = "http://localhost:8080/api/accounts/" + iban;
        try {
            account = restTemplate.getForObject(url, Account.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }    
    //Scenario: Successfully insert an account
    @And("the request body contains valid account data")
    public void the_request_body_contains_valid_account_data() {
        accountRequestDTO = new AccountRequestDTO(1L, new BigDecimal(10), AccountType.CURRENT.getValue());
    }
    @When("a request is made to POST {string}")
    public void a_request_is_made_to_post_accounts(String path) {
        String url = "http://localhost:8080" + path;
        response = restTemplate.postForEntity(url, accountRequestDTO, Account[].class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @And("the response body should contain the inserted account object")
    public void the_response_body_should_contain_the_inserted_account_object() {
        assertEquals(account.getIban(), response.getBody()[0].getIban());
    }
    //Scenario: Attempt to insert an account with invalid data
    @And("the request body contains invalid account data")
    public void the_request_body_contains_invalid_account_data() {
        accountRequestDTO = new AccountRequestDTO(1L, new BigDecimal(10), AccountType.CURRENT.getValue());
    }
    //Scenario: Successfully update an account
    @When("a request is made to PUT {string}")
    public void a_request_is_made_to_put_accounts(String path) {
        String url = "http://localhost:8080" + path;
        response = restTemplate.postForEntity(url, accountRequestDTO, Account[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @And("the response body should contain the updated account object")
    public void the_response_body_should_contain_the_updated_account_object() {
        assertEquals(account.getIban(), response.getBody()[0].getIban());
    }
    //Scenario: Successfully delete an account
    @When("a request is made to DELETE {string}")
    public void a_request_is_made_to_delete_accounts(String path) {
        String url = "http://localhost:8080" + path;
        restTemplate.delete(url);
    }
}

//test

    //Scenario: Successfully get all accounts
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }

    //Scenario: Successfully get an account by iban
    // @Given("the API is running")
    // public void the_api_is_running() {
    // should be running
    // }
    // @When("a request is made to GET {string}")
    // public void a_request_is_made_to_get_accounts(String path) {
    //     String url = "http://localhost:8080" + path;
    //     response = restTemplate.getForEntity(url, Account[].class);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //    assertEquals(responseStatus, response.getStatusCodeValue());
    // }

    //Scenario: Attempt to get a non-existent account by IBAN
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }
    // @When("a request is made to GET {string}")
    // public void a_request_is_made_to_get_accounts(String path) {
    //     String url = "http://localhost:8080" + path;
    //     response = restTemplate.getForEntity(url, Account[].class);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }

    //Scenario: Successfully insert an account
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }

    //Scenario: Successfully insert an account

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }

    //Scenario: Attempt to insert an account with invalid data
    // @Given("the API is running")
    // public void the_api_is_running() {
    // should be running
    // }
    // @When("a request is made to POST {string}")
    // public void a_request_is_made_to_post_accounts(String path) {
    //     String url = "http://localhost:8080" + path;
    //     response = restTemplate.postForEntity(url, accountRequestDTO, Account[].class);
    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }
    
    //Scenario: Successfully update an account
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }
    // @And("there is an account with IBAN {string}")
    // public void there_is_an_account_with_iban(String iban) {
    //     String url = "http://localhost:8080/api/accounts/" + iban;
    //     account = restTemplate.getForObject(url, Account.class);
    //     restTemplate.postForEntity(url, account, Account.class);
    // }
    // @And("the request body contains valid account data")
    // public void the_request_body_contains_valid_account_data() {
    //     accountRequestDTO = new AccountRequestDTO(1L, new BigDecimal(10), AccountType.CURRENT.getValue());
    // }
    //Scenario: Successfully update an account
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }

    //Scenario: Attempt to update a non-existent account
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }
    // @And("there is no account with IBAN {string}")
    // public void there_is_no_account_with_iban(String iban) {
    //     String url = "http://localhost:8080/api/accounts/" + iban;
    //     try {
    //         account = restTemplate.getForObject(url, Account.class);
    //     } catch (HttpClientErrorException e) {
    //         assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    //     }
    // }
    // @And("the request body contains valid account data")
    // public void the_request_body_contains_valid_account_data() {
    //     accountRequestDTO = new AccountRequestDTO(1L, new BigDecimal(10), AccountType.CURRENT.getValue());
    // }
    // @When("a request is made to PUT {string}")
    // public void a_request_is_made_to_put_accounts(String path) {
    //     String url = "http://localhost:8080" + path;
    //     response = restTemplate.postForEntity(url, accountRequestDTO, Account[].class);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }
    // @And("the response body should contain the updated account object")
    // public void the_response_body_should_contain_the_updated_account_object() {
    //     assertEquals(account.getIban(), response.getBody()[0].getIban());
    // }

    //Scenario: Successfully delete an account
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }
    // @And("there is an account with IBAN {string}")
    // public void there_is_an_account_with_iban(String iban) {
    //     String url = "http://localhost:8080/api/accounts/" + iban;
    //     account = restTemplate.getForObject(url, Account.class);
    //     restTemplate.postForEntity(url, account, Account.class);
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }

    //Scenario: Attempt to delete a non-existent account
    // @Given("the API is running")
    // public void the_api_is_running() {
    //     // should be running
    // }
    // @And("there is no account with IBAN {string}")
    // public void there_is_no_account_with_iban(String iban) {
    //     String url = "http://localhost:8080/api/accounts/" + iban;
    //     try {
    //         account = restTemplate.getForObject(url, Account.class);
    //     } catch (HttpClientErrorException e) {
    //         assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    //     }
    // }
    // @When("a request is made to DELETE {string}")
    // public void a_request_is_made_to_delete_accounts(String path) {
    //     String url = "http://localhost:8080" + path;
    //     restTemplate.delete(url);
    // }
    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(Integer responseStatus) {
    //     assertEquals(responseStatus, response.getStatusCodeValue());
    // }
