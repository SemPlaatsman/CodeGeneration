package nl.inholland.codegeneration.controllers;

import java.util.List;

import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.TransactionService;

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
        List<TransactionResponseDTO> transactions = transactionService.getAll(queryParams);
        return ResponseEntity.status(200).body(transactions);
    }


    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable Long id) {
        TransactionResponseDTO transaction = transactionService.getById(id);
        return ResponseEntity.status(200).body(transaction);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR (hasAuthority('CUSTOMER') AND #transaction.accountFrom.user.id == authentication.principal.id)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO addedTransaction = transactionService.add(transactionRequestDTO);
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