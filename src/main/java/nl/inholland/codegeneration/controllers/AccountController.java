package nl.inholland.codegeneration.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // get /accounts
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "filter", required = false) String filterQuery)
            throws Exception {

        QueryParams queryParams = new QueryParams(Transaction.class);
        queryParams.setFilter(filterQuery);
        List<Account> accounts = accountService.getAll(queryParams);
        return ResponseEntity.status(200).body(accounts);

    }

    // get /accounts/{Iban}
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountByIban(@PathVariable("iban") String Iban) throws APIException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = CustomerIbanCheck(user, Iban);
        return ResponseEntity.status(200).body(account);

    }

    // post /accounts
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertAccount(@RequestBody Account account) throws APIException {

        Optional<User> user = (Optional<User>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User _user = user.orElseThrow(() -> new APIException("User not found", HttpStatus.UNAUTHORIZED, LocalDateTime.now()));
    
        CustomerIbanCheck(_user, account.getIban());

        account.setUser(_user);
        Account _account = accountService.insertAccount(account);
        return ResponseEntity.status(201).body(_account);

    }

    // put /accounts/{Ibans}
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{Iban}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable("Iban") String Iban)
            throws APIException {
        Account _account = accountService.updateAccount(account, Iban);
        return ResponseEntity.status(200).body(_account);

    }

    // delete /accounts/{Iban}
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping(path = "/{Iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("Iban") String Iban) throws APIException {
        accountService.deleteAccount(Iban);
        return ResponseEntity.status(204).body(null);

    }

    // get /accounts/{iban}/transactions
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{Iban}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@PathVariable("Iban") String Iban) throws APIException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerIbanCheck(user, Iban);
        List<Transaction> accounts = accountService.getTransactions(Iban);
        return ResponseEntity.status(200).body(accounts);

    }

    // get /accounts/{id}/balance
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{Iban}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("Iban") String Iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerIbanCheck(user, Iban);
        BigDecimal balance = accountService.getBalance(Iban);
        return ResponseEntity.status(200).body(balance);

    }

    private Account CustomerIbanCheck(User user, String Iban) throws APIException {
        Account account = accountService.getAccountByIban(Iban)
                .orElseThrow(() -> new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now()));
        if (account.getUser() != user && user.getRole() == Role.CUSTOMER) {
            throw new APIException("no acces", HttpStatus.FORBIDDEN, null);
        }
        return account;
    }
}
