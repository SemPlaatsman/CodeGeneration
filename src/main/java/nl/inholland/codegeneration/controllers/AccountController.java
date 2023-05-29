package nl.inholland.codegeneration.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.EqualsAndHashCode.Include;

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
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // get /accounts
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "filter", required = false) String filterQuery)
            throws Exception {
        QueryParams queryParams = new QueryParams(Transaction.class);
        queryParams.setFilter(filterQuery);
        List<Account> accounts = accountService.getAll(queryParams);
        return ResponseEntity.status(200).body(accounts);
    }

    // get /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountByIban(@PathVariable("iban") String iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = CustomerIbanCheck(user, iban);
        return ResponseEntity.status(200).body(account);
    }

    // post /accounts
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertAccount(@RequestBody Account account) throws APIException {

        User user = ((Optional<User>) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orElseThrow(() -> new APIException("User not found", HttpStatus.UNAUTHORIZED, null));
    
        CustomerIbanCheck(user, account.getIban());

        account.setUser(user);
        Account addedAccount = accountService.insertAccount(account);
        return ResponseEntity.status(201).body(addedAccount);


    }

    // put /accounts/{iban}
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable("iban") String iban)
            throws APIException {
        Account updatedAccount = accountService.updateAccount(account, iban);

        return ResponseEntity.status(200).body(updatedAccount);

    }

    // delete /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("iban") String iban) throws APIException {
        accountService.deleteAccount(iban);

        return ResponseEntity.status(204).body(null);
    }

    // get /accounts/{iban}/transactions
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{iban}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@PathVariable("iban") String iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerIbanCheck(user, iban);
        List<Transaction> accounts = accountService.getTransactions(iban);
        return ResponseEntity.status(200).body(accounts);
    }

    // get /accounts/{id}/balance
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping(path = "/{iban}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBalance(@PathVariable("iban") String iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerIbanCheck(user, iban);
        BigDecimal balance = accountService.getBalance(iban);
        return ResponseEntity.status(200).body(balance);


    }

    private Account CustomerIbanCheck(User user, String iban) throws APIException {
        Account account = accountService.getAccountByIban(iban)
                .orElseThrow(() -> new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now()));
        if (account.getUser() != user && user.getRoles().contains(Role.CUSTOMER)) {
            throw new APIException("Forbidden!", HttpStatus.FORBIDDEN, null);
        }
        return account;
    }
}
