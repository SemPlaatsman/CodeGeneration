// Path: src\test\java\nl\inholland\codegeneration\stepdefinitions\TransactionSteps.java

package nl.inholland.codegeneration.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.services.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class TransactionSteps {
    Transaction transaction = new Transaction();
    private Account myAccount;
    private Account theirAccount;
    TransactionService transactionService;

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

                // TransactionResponseDTO addedTransaction = transactionService.add(transactionRequestDTO);

        myAccount.setBalance(myAccount.getBalance().subtract(transaction.getAmount()));
        theirAccount.setBalance(theirAccount.getBalance().add(transaction.getAmount()));

        

    }

    @Then("I should have {int} on my bank account")
    public void i_should_have_on_my_bank_account(Integer expectedBalance) {
        assertEquals(expectedBalance.intValue(), myAccount.getBalance().intValue());
    }
}