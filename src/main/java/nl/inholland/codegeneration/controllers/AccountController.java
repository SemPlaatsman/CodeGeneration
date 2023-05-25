package nl.inholland.codegeneration.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private  AccountService accountService;

  

    // get /accounts
    @PreAuthorize("hasAuthority('CUSTOMER') AND hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "filter", required = false) String filterQuery) throws Exception {
        try {
            QueryParams queryParams = new QueryParams(Transaction.class);
            queryParams.setFilter(filterQuery);
            List<Account> accounts = accountService.getAll(queryParams);
            return ResponseEntity.status(200).body(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // get /accounts/{Iban}
    @PreAuthorize("hasAuthority('CUSTOMER') AND hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountByIban(@PathVariable("iban") String iban) {
        try {

            Account account = accountService.getAccountByIban(iban).get();
            return ResponseEntity.status(200).body(account);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post /accounts
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertAccount(@RequestBody Account account) {
        try{

            Account _account = accountService.insertAccount(account);
            return ResponseEntity.status(201).body(_account);

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // put /accounts/{Ibans}
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{Iban}", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable("Iban") String Iban) {
        try{
        Account _account = accountService.updateAccount(account, Iban);
        return ResponseEntity.status(200).body(_account);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete /accounts/{Iban}
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping(path = "/{Iban}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("Iban") String Iban) {
        try {
            accountService.deleteAccount(Iban);
            return  ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // /accounts/{iban}/transactions
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{Iban}/transactions", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@PathVariable("Iban") String Iban) {
        try {
            List<Transaction> accounts = accountService.getTransactions(Iban);
            return ResponseEntity.status(200).body(accounts);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get /accounts/{id}/balance
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{Iban}/balance", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("Iban") String Iban) {
        try {
            BigDecimal balance = accountService.getBalance(Iban);
            return ResponseEntity.status(200).body(balance);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
