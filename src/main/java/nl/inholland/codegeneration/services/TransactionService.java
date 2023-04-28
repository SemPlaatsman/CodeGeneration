package nl.inholland.codegeneration.services;

import java.util.List;

import nl.inholland.codegeneration.models.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction add(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAll(@Nullable QueryParams queryParams) {
        return transactionRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }

    public Transaction getById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction update(Transaction transaction, long id) {
        Transaction existingTransaction = getById(id);
        existingTransaction.update(transaction);
        return transactionRepository.save(existingTransaction);
    }
}
