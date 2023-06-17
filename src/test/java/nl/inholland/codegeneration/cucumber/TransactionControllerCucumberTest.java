// Path: src\test\java\nl\inholland\codegeneration\cucumber\TransactionControllerCucumberTest.java

package nl.inholland.codegeneration.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"nl.inholland.codegeneration.stepdefinitions"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)

public class TransactionControllerCucumberTest {

  // private Account myAccount;
  // private Account theirAccount;
  // private Transaction transaction;

  // private TransactionRequestDTO transactionRequestDTO;

  // @Given("I have {int} on my bank account")
  // public void i_have_on_my_bank_account(Integer amount) {
  //   myAccount = new Account();
  //   myAccount.setBalance(BigDecimal.valueOf(amount));
  //   myAccount.setIban("NL01INHO0000000001");
  // }

  // @When("I transfer {int} to the other bank account")
  // public void i_transfer_to_the_other_bank_account(Integer amount) {
  //   theirAccount = new Account();
  //   theirAccount.setIban("NL01INHO0000000002");

  //   transaction = new Transaction();
  //   transaction.setAmount(BigDecimal.valueOf(amount));
  //   transaction.setAccountFrom(myAccount);
  //   transaction.setAccountTo(theirAccount);

  //   // code to execute the transaction is needed here

  //   TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(myAccount.getIban(), theirAccount.getIban(), new BigDecimal(100), "description");
  // }

  // @Then("I should have {int} on my bank account")
  // public void i_should_have_on_my_bank_account(Integer amount) {
  //   assertEquals(amount.intValue(), myAccount.getBalance().intValue());
  // }

}
