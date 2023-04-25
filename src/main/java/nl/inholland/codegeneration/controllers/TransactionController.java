package nl.inholland.codegeneration.controllers;

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

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable int id) {
        return transactionService.getTransactionId(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@RequestBody Transaction transaction, @PathVariable int id) {
        return transactionService.updateTransaction(transaction, id);
    }  

    @PutMapping("/{id}/delete")
    public Transaction deleteTransaction(@PathVariable int id) {
        return transactionService.deleteTransaction(id);
    }
}
    // @Autowired
    // private TransactionRepository transactionRepository;

    // @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<List<Transaction>> getAll() {
    //     List<Transaction> transactions = transactionRepository.findAll();
    //     return ResponseEntity.status(200).body(transactions);
    // }

    // @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<Transaction> insertTransaction(@RequestBody Transaction transaction) {
    //     Transaction _transaction = transactionRepository.save(new Transaction(null, transaction.getTimestamp(), transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount(), transaction.getPerformingUser(), transaction.getDescription(), false));
    //     return ResponseEntity.status(201).body(_transaction);
    // }

    // @PutMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<Transaction> deleteTransaction(@PathVariable Long id) {
    //     Transaction transaction = transactionRepository.findById(id).get();
    //     transaction.setIsDeleted(true);
    //     transactionRepository.save(transaction);
    //     return ResponseEntity.status(200).body(transaction);
    // }
