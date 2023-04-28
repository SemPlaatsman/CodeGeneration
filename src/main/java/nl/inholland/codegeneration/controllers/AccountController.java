package nl.inholland.codegeneration.controllers;

import java.util.List;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private  AccountService accountService;

    // get /accounts
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        try {
            List<Account> accounts = accountService.getAll();
            return ResponseEntity.status(200).body(accounts);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/{Ibans}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountByIban(@PathVariable("Ibans") String Ibans) {
        try {
            Account account = accountService.getAccountByIban(Ibans);
            return ResponseEntity.status(200).body(account);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // post /accounts
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertAccount(@RequestBody Account account) {
        try{
            System.out.println(account.getAbsoluteLimit());
        Account _account = accountService.insertAccount(account);
        return ResponseEntity.status(201).body(_account);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    




    // get /accounts/{id}/transaction
    @PostMapping(path = "/transaction", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getTransaction(@RequestBody Account account, @RequestParam("iban") double amount) {
        System.out.println("getTransaction"+ account + amount);
        return null;
    }

    // get /accounts/{id}/balance
    @PostMapping(path = "/balance", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getBalance(@RequestBody Account account, @RequestParam("iban") double amount) {

        return null;
    }
}
