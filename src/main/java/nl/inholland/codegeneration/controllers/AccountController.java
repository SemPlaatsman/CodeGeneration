package nl.inholland.codegeneration.controllers;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // get /accounts
    @PreAuthorize("hasAuthority('EMPLOYEE')")
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

    // get /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
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
    
    // put /accounts/{iban}
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{iban}", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable("iban") String iban) {
        try{
        Account _account = accountService.updateAccount(account, iban);
        return ResponseEntity.status(200).body(_account);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping(path = "/{iban}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("iban") String iban) {
        try {
            accountService.deleteAccount(iban);
            return  ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // /accounts/{iban}/transactions
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{iban}/transactions", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@PathVariable("iban") String iban) {
        try {
            List<Transaction> accounts = accountService.getTransactions(iban);
            return ResponseEntity.status(200).body(accounts);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get /accounts/{id}/balance
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{iban}/balance", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("iban") String iban) {
        try {
            BigDecimal balance = accountService.getBalance(iban);
            return ResponseEntity.status(200).body(balance);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
