package nl.inholland.codegeneration.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Transaction getTransactionId(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction updateTransaction(Transaction transaction, long id) {
        Transaction existingTransaction = getTransactionId(id);

        existingTransaction.setTimestamp(transaction.getTimestamp());
        existingTransaction.setAccountFrom(transaction.getAccountFrom());
        existingTransaction.setAccountTo(transaction.getAccountTo());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setPerformingUser(transaction.getPerformingUser());
        existingTransaction.setDescription(transaction.getDescription());

        return transactionRepository.save(existingTransaction);
    }

    public Transaction deleteTransaction(long id) {
        Transaction existingTransaction = getTransactionId(id);
        if (existingTransaction != null) {
            existingTransaction.setIsDeleted(true);
            return transactionRepository.save(existingTransaction);
        }
        return null;
    }
}
