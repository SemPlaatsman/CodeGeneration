package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.*;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDTOMapper transactionDTOMapper;

    public List<TransactionResponseDTO> getAll(@Nullable QueryParams<Transaction> queryParams) {
        return (List<TransactionResponseDTO>) transactionRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent().stream().map(transactionDTOMapper.toResponseDTO).collect(Collectors.toList());
    }

    public TransactionResponseDTO getById(Long i) {
        Transaction transaction = transactionRepository.findById(i).orElseThrow(() -> new EntityNotFoundException("Transaction not found!"));
        return transactionDTOMapper.toResponseDTO.apply(transaction);
    }

    @Transactional
    public TransactionResponseDTO add(TransactionRequestDTO transactionRequestDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = transactionDTOMapper.toTransaction.apply(transactionRequestDTO);
        if (transaction.getAccountFrom().getIsDeleted() || transaction.getAccountTo().getIsDeleted()) {
            throw new InvalidDataAccessApiUsageException("Invalid bank account provided!");
        } else if (transaction.getAccountFrom().getAbsoluteLimit().compareTo(transaction.getAccountFrom().getBalance().subtract(transaction.getAmount())) > 0) {
            throw new IllegalStateException("Insufficient balance!");
        } else if (transactionRepository.findDailyTransactionsValueOfUser(transaction.getAccountFrom().getUser().getId()).orElse(new BigDecimal(0)).add(transaction.getAmount()).compareTo(transaction.getAccountFrom().getUser().getDayLimit()) > 0) {
            throw new IllegalStateException("Amount cannot surpass day limit!");
        } else if (transaction.getAmount().compareTo(user.getTransactionLimit()) > 0) {
            throw new IllegalStateException("Amount cannot surpass transaction limit!");
        } else if (transaction.getAccountFrom().getAccountType() == AccountType.SAVINGS && transaction.getAccountFrom().getUser().getId() == transaction.getAccountTo().getUser().getId()) {
            throw new IllegalStateException("Cannot make a transaction from a savings account to an account that is not of the same user!");
        }
        transaction.getAccountFrom().setBalance(transaction.getAccountFrom().getBalance().subtract(transaction.getAmount()));
        transaction.getAccountTo().setBalance(transaction.getAccountTo().getBalance().add(transaction.getAmount()));
        transaction.setId(null);
        transaction.setPerformingUser(user);
        transaction.setTimestamp(LocalDateTime.now());
        return transactionDTOMapper.toResponseDTO.apply(transactionRepository.save(transaction));
    }

//    public Transaction update(Transaction transaction, long id) {
//        Transaction existingTransaction = getById(id);
//        existingTransaction.update(transaction);
//        return transactionRepository.save(existingTransaction);
//    }
}
