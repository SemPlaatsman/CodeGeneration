package nl.inholland.codegeneration.controllers;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.QueryParams;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
// @CrossOrigin("http://localhost:5173/")
// @CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(@Valid QueryParams queryParams) {
        try {
            List<Transaction> transactions = transactionService.getAll(queryParams);
            return ResponseEntity.status(200).body(transactions);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }    
    
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable int id) {
        try {
            Transaction transaction = transactionService.getById(id);
            return ResponseEntity.status(200).body(transaction);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }    
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody Transaction transaction) {
        try {
            Transaction addedTransaction = transactionService.add(transaction);
            return ResponseEntity.status(201).body(addedTransaction);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }    
    
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody Transaction transaction, @PathVariable int id) {
        try {
            Transaction updatedTransaction = transactionService.update(transaction, id);
            return ResponseEntity.status(200).body(updatedTransaction);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }    
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