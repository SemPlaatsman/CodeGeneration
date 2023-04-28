package nl.inholland.codegeneration.controllers;

import java.util.List;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private  AccountService accountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAll(@Valid QueryParams queryParams) {
        List<Account> accounts = accountService.getAll(queryParams);
        return ResponseEntity.status(200).body(accounts);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> insertAccount(@RequestBody Account account) {
        Account _account = accountService.insertAccount(account);
        return ResponseEntity.status(201).body(_account);
    }
    
}
