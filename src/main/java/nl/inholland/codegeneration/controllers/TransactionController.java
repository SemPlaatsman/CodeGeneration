package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.status(200).body(transactions);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> insertTransaction(@RequestBody Transaction transaction) {
        Transaction _transaction = transactionRepository.save(new Transaction(null, transaction.getTimestamp(), transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount(), transaction.getPerformingUser(), transaction.getDescription(), false));
        return ResponseEntity.status(201).body(_transaction);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id).get();
        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);
        return ResponseEntity.status(200).body(transaction);
    }

}
