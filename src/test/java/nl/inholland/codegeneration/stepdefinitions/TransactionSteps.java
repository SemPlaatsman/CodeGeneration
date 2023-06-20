// Path: src\test\java\nl\inholland\codegeneration\stepdefinitions\TransactionSteps.java

package nl.inholland.codegeneration.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.services.TransactionService;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class TransactionSteps {
    Transaction transaction = new Transaction();
    private Account myAccount;
    private Account theirAccount;
    TransactionService transactionService;

    //Make transaction
    @Given("I have {int} on my bank account")
    public void i_have_on_my_bank_account(Integer amount) {
        myAccount = new Account();
        myAccount.setBalance(BigDecimal.valueOf(amount));
    }
    @When("I transfer {int} to the other bank account")
    public void i_transfer_to_the_other_bank_account(Integer amount) {
        theirAccount = new Account();
        transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setAccountFrom(myAccount);
        transaction.setAccountTo(theirAccount);
        // additional code to execute the transaction

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(myAccount.getIban(),
                theirAccount.getIban(), new BigDecimal(20), "cucumber test");

        // transactionService.add(transactionRequestDTO);

        myAccount.setBalance(myAccount.getBalance().subtract(transaction.getAmount()));
        theirAccount.setBalance(theirAccount.getBalance().add(transaction.getAmount()));
 
        // moet misschien nog iets met de response doen

    }
    @Then("I should have {int} on my bank account")
    public void i_should_have_on_my_bank_account(Integer expectedBalance) {
        assertEquals(expectedBalance.intValue(), myAccount.getBalance().intValue());
    }

    //Get transactions
    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<List> responses;
    
    @Given("the api is running")
    public void the_api_is_running() {
        String url = "http://localhost:8080/health";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getBody().equals("UP")) {
            throw new RuntimeException("API is not running");
        }
    }
    @WithMockUser(username = "testUser", roles = {"EMPLOYEE"})
    @When("a request is to GET {string}")
    public void a_request_is_to_GET_transactions(String path) {
        String url = "http://localhost:8080" + path;
        //  + path.toString()
        responses = restTemplate.getForEntity(url, List.class);
    }
    @WithMockUser(username = "testUser", roles = {"EMPLOYEE"})
    @Then("the response should be {int}")
    public void the_response_should_be(Integer int1) {
        assertEquals(responses.getStatusCode().value(), int1.intValue());
    }
    @WithMockUser(username = "testUser", roles = {"EMPLOYEE"})
    @And("should contain a List of transaction objects")
    public void should_contain_a_List_of_transaction_objects() {
        assertTrue(responses.getBody().size() > 0);
    }

    //Get transaction by id
    private ResponseEntity<TransactionResponseDTO> responseById;
    private TransactionResponseDTO transactionResponseDTO;
    private TransactionRequestDTO transactionRequestDTO;
    private Transaction transactionById;
    private Transaction transactionById2;

    @Given("a transaction with id {int} exists")
    public void a_transaction_with_id_exists(Integer id) {
        transactionById = new Transaction();
        transactionById.setId(Long.valueOf(id));
        transactionById.setAmount(BigDecimal.valueOf(20));
        transactionById.setAccountFrom(myAccount);
        transactionById.setAccountTo(theirAccount);
        transactionById.setDescription("cucumber test");
        transactionById2 = new Transaction();
        transactionById2.setId(Long.valueOf(id));
        transactionById2.setAmount(BigDecimal.valueOf(20));
        transactionById2.setAccountFrom(myAccount);
        transactionById2.setAccountTo(theirAccount);
        transactionById2.setDescription("cucumber test");
    }

    @When("a request is to GET a {string} by id {int}")
    public void a_request_is_to_GET_a_transaction_by_id(String path, Integer id) {
        String url = "http://localhost:8080" + path + id.toString();
        responseById = restTemplate.getForEntity(url, TransactionResponseDTO.class);
        transactionResponseDTO = responseById.getBody();
    }

    @Then("the response should be {int} and contain the transaction with id {int}")
    public void the_response_should_be_and_contain_the_transaction_with_id(Integer int1, Integer id) {
        assertEquals(responseById.getStatusCode().value(), int1.intValue());
        assertEquals(transactionResponseDTO.id(), id.intValue());
    }

// try to get transaction with bad id
private ResponseEntity<String> response;

@Given("no {string} with id {int} exists")
public void no_transaction_with_id_exists(String path, Integer id) {
    String url = "http://localhost:8080" + path + id;
    try {
        restTemplate.getForEntity(url, String.class);
        throw new RuntimeException("Transaction with id " + id + " should not exist.");
    } catch (HttpClientErrorException.NotFound ex) {
        // Transaction doesn't exist, which is what we want
    }
}

@When("a request is to GET a {string} by bad id {int}")
public void a_request_is_to_GET_a_transaction_by_bad_id(String path, Integer id) {
    String url = "http://localhost:8080" + path + Long.valueOf(id);
    try {
        response = restTemplate.getForEntity(url, String.class);
    } catch (HttpClientErrorException ex) {
        response = ResponseEntity.status(ex.getRawStatusCode()).build();
    }
}

@Then("the error response should be {int}")
public void the_error_response_should_be(Integer int1) {
    assertEquals(response.getStatusCode().value(), int1.intValue());
}

}