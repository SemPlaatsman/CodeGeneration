package nl.inholland.codegeneration.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.repositories.AccountRepository;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private  AccountRepository accountRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return ResponseEntity.status(200).body(accounts);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> insertAccount(@RequestBody Account account) {
        Account _account = accountRepository.save(new Account( 0, account.getIban(), account.getAccountType(), account.getCustomer(), account.getBalance(), account.getAbsoluteLimit()));
        return ResponseEntity.status(201).body(_account);
    }
    
}
