package nl.inholland.codegeneration.controllers;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "filter", required = false) String filterQuery) throws Exception {
        QueryParams queryParams = new QueryParams(Transaction.class);
        queryParams.setFilter(filterQuery);
        List<Transaction> transactions = transactionService.getAll(queryParams);
        return ResponseEntity.status(200).body(transactions);
    }


    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable int id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.status(200).body(transaction);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR (hasAuthority('CUSTOMER') AND #transaction.accountFrom.user.id == authentication.principal.id)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Transaction transaction) {
        Transaction addedTransaction = transactionService.add(transaction);
        return ResponseEntity.status(201).body(addedTransaction);
    }
    
//    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> update(@RequestBody Transaction transaction, @PathVariable int id) { //add a type where the question mark is if applicable
//        try {
//            Transaction updatedTransaction = transactionService.update(transaction, id);
//            return ResponseEntity.status(200).body(updatedTransaction);
//        } catch (Exception ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        }
//    }
}
// @GetMapping
// public List<Transaction> getAllTransactions() {
// return transactionService.getTransactions();
// }
// public Transaction getById(@PathVariable int id) {
// return transactionService.getById(id);
// }
// @PostMapping
// public Transaction createTransaction(@RequestBody Transaction transaction) {
// return transactionService.add(transaction);
// }
// @PutMapping("/{id}")
// public Transaction update(@RequestBody Transaction transaction, @PathVariable
// int id) {
// return transactionService.update(transaction, id);
// }