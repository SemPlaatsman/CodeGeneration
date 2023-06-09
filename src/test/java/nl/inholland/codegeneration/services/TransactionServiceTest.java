package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.models.*;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionDTOMapper transactionDTOMapper;
    @InjectMocks
    private TransactionService transactionService;

    private TransactionRequestDTO transactionRequestDTO;
    private Transaction validTransaction;
    private TransactionResponseDTO transactionResponseDTO;
    private User user;
    private Account accountFrom;
    private Account accountTo;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setDayLimit(BigDecimal.valueOf(5000));
        user.setTransactionLimit(BigDecimal.valueOf(2000));

        accountFrom = new Account();
        accountFrom.setUserById(1L);
        accountFrom.setIban("accountFromIban");
        accountFrom.setUser(user);
        accountFrom.setBalance(BigDecimal.valueOf(2000));
        accountFrom.setIsDeleted(false);
        accountFrom.setAbsoluteLimit(BigDecimal.valueOf(0));
        accountFrom.setAccountType(AccountType.CURRENT);

        accountTo = new Account();
        accountTo.setUserById(2L);
        accountTo.setIban("accountToIban");
        accountTo.setUser(user);
        accountTo.setBalance(BigDecimal.valueOf(1000));
        accountTo.setIsDeleted(false);

        transactionRequestDTO = new TransactionRequestDTO("accountFromIban", "accountToIban", BigDecimal.valueOf(100), "description");
        validTransaction = new Transaction();
        validTransaction.setAccountFrom(accountFrom);
        validTransaction.setAccountTo(accountTo);
        validTransaction.setAmount(BigDecimal.valueOf(100));
        validTransaction.setPerformingUser(user);
        validTransaction.setTimestamp(LocalDateTime.now());

        transactionResponseDTO = new TransactionResponseDTO(validTransaction);
    }

    @Test
    public void add_ValidTransaction_Success() {
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);
        when(transactionRepository.findDailyTransactionsValueOfUser(anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(0)));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(validTransaction);
        when(transactionDTOMapper.toResponseDTO.apply(any(Transaction.class))).thenReturn(transactionResponseDTO);

        TransactionResponseDTO addedTransaction = transactionService.add(transactionRequestDTO);

        assertThat(addedTransaction).isEqualToComparingFieldByField(transactionResponseDTO);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void add_InsufficientBalance_ThrowsIllegalStateException() {
        accountFrom.setBalance(BigDecimal.valueOf(50)); // Set balance lower than transaction amount
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);

        assertThatThrownBy(() -> transactionService.add(transactionRequestDTO)).isInstanceOf(IllegalStateException.class).hasMessage("Insufficient balance!");
    }

    @Test
    public void add_DeletedAccount_ThrowsInvalidDataAccessApiUsageException() {
        accountFrom.setIsDeleted(true); // Mark account as deleted
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);

        assertThatThrownBy(() -> transactionService.add(transactionRequestDTO)).isInstanceOf(InvalidDataAccessApiUsageException.class).hasMessage("Invalid bank account provided!");
    }

   

    }


