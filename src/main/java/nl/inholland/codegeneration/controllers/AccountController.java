package nl.inholland.codegeneration.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.EqualsAndHashCode.Include;

import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
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

import jakarta.validation.Valid;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.BalanceResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // get /accounts
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "filter", required = false) String filterQuery,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    @RequestParam(value = "page", required = false) Integer page)
            throws Exception {
        QueryParams<Account> queryParams = new QueryParams(Account.class, limit, page);
        queryParams.setFilter(filterQuery);
        List<AccountResponseDTO> accounts = accountService.getAll(queryParams);
        return ResponseEntity.status(200).body(accounts);
    }

    // get /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountByIban(@PathVariable("iban") String iban) throws APIException {
        // Account account = CustomerIbanCheck(user, iban);
        
        AccountResponseDTO account = accountService.getAccountByIban(iban);
        return ResponseEntity.status(200).body(account);
    }

    // post /accounts
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertAccount(@RequestBody @Valid AccountRequestDTO account) throws APIException {

        AccountResponseDTO addedAccount = accountService.insertAccount(account);
        return ResponseEntity.status(201).body(addedAccount);
    }

    // put /accounts/{iban}
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@RequestBody @Valid AccountRequestDTO account, @PathVariable("iban") String iban)
            throws APIException {
        AccountResponseDTO updatedAccount = accountService.updateAccount(account, iban);
        return ResponseEntity.status(200).body(updatedAccount);

    }

    // delete /accounts/{iban}
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @DeleteMapping(path = "/{iban}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("iban") String iban) throws APIException {
        accountService.deleteAccount(iban);
        return ResponseEntity.status(204).body(null);
    }

    // get /accounts/{iban}/transactions
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@RequestParam(value = "filter", required = false) String filterQuery,
                                             @RequestParam(value = "limit", required = false) Integer limit,
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @PathVariable("iban") String iban) throws Exception {
        QueryParams<Transaction> queryParams = new QueryParams(Transaction.class, limit, page);
        queryParams.setFilter(filterQuery);
        List<TransactionResponseDTO> accounts = accountService.getTransactions(queryParams, iban);
        return ResponseEntity.status(200).body(accounts);
    }

    // get /accounts/{id}/balance
    @PreAuthorize("hasAuthority('CUSTOMER') OR hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{iban}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBalance(@PathVariable("iban") String iban) throws APIException {
       
        BalanceResponseDTO balance = accountService.getBalance(iban);
        return ResponseEntity.status(200).body(balance);
    }
 
}
