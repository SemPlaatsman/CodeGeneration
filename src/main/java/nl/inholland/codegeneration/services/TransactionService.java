package nl.inholland.codegeneration.services;

import java.time.LocalDateTime;
import java.util.List;


import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.repositories.TransactionRepository;

import javax.naming.InsufficientResourcesException;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAll(@Nullable QueryParams queryParams) {
        return transactionRepository.findAll(PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }

    public Transaction getById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction add(Transaction transaction) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (transaction.getAccountFrom().getAbsoluteLimit().compareTo(transaction.getAccountFrom().getBalance().subtract(transaction.getAmount())) > 0) {
            throw new IllegalStateException("Insufficient balance!");
        }
        else if (transactionRepository.findDailyTransactionsValueOfUser(transaction.getAccountFrom().getUser().getId()).add(transaction.getAmount()).compareTo(transaction.getAccountFrom().getUser().getDayLimit()) > 0) {
            throw new IllegalStateException("Amount cannot surpass day limit!");
        }
        else if (transaction.getAmount().compareTo(user.getTransactionLimit()) > 0) {
            throw new IllegalStateException("Amount cannot surpass transaction limit!");
        }
        else if (transaction.getAccountFrom().getAccountType() == AccountType.SAVINGS && transaction.getAccountFrom().getUser().getId() == transaction.getAccountTo().getUser().getId()) {
            throw new IllegalStateException("Cannot make a transaction from a savings account to an account that is not of the same user!");
        }
        else if (user.getRole() == Role.CUSTOMER && transaction.getAccountFrom().getUser().getId() != user.getId()) {
            throw new InsufficientAuthenticationException("Forbidden!");
        }
        transaction.setId(null);
        transaction.setPerformingUser(user);
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

//    public Transaction update(Transaction transaction, long id) {
//        Transaction existingTransaction = getById(id);
//        existingTransaction.update(transaction);
//        return transactionRepository.save(existingTransaction);
//    }
}
