package nl.inholland.codegeneration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    TransactionDTOMapper transactionDTOMapper;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    TransactionService transactionService;

    private TransactionRequestDTO transactionRequestDTO;
    private Transaction validTransaction;
    private TransactionResponseDTO transactionResponseDTO;
    private User user;
    private Account accountFrom;
    private Account accountTo;

    private User AuthenticationUser = new User(null, null, null, null, null, null, null, null, null, null, null, null);

    @BeforeEach
    public void setup() {
        user = new User(1L, null, null, null, null, null, 
        null, null, null, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000), null);
       
       
        accountFrom = new  Account(String iban, AccountType accountType, User user, BigDecimal balance, BigDecimal absoluteLimit, Boolean isDeleted);
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

        transactionRequestDTO = new TransactionRequestDTO("accountFromIban", "accountToIban", BigDecimal.valueOf(100),
                "description");
        validTransaction = new Transaction();
        validTransaction.setAccountFrom(accountFrom);
        validTransaction.setAccountTo(accountTo);
        validTransaction.setAmount(BigDecimal.valueOf(100));
        validTransaction.setPerformingUser(user);
        validTransaction.setTimestamp(LocalDateTime.now());

        // security mocks

        AuthenticationUser.setUsername("sarawilson");
        AuthenticationUser.setPassword("sara123");
        AuthenticationUser.setRoles(Collections.singletonList(Role.EMPLOYEE)); // Assuming the user has the role
                                                                               // "EMPLOYEE"

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                AuthenticationUser,
                "sara123",
                AuthenticationUser.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        transactionDTOMapper = new TransactionDTOMapper(accountRepository);
        transactionDTOMapper.toTransaction = Mockito.mock(Function.class);
        transactionDTOMapper.toResponseDTO = Mockito.mock(Function.class);
    }

    @Test
    public void add_ValidTransaction_Success() {

        
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);
        when(transactionRepository.findDailyTransactionsValueOfUser(anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(0)));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(validTransaction);
        when(transactionDTOMapper.toResponseDTO.apply(any(Transaction.class))).thenReturn(transactionResponseDTO);

        TransactionResponseDTO addedTransaction = transactionService.add(transactionRequestDTO);



        assertEquals(addedTransaction, transactionResponseDTO);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void add_InsufficientBalance_ThrowsIllegalStateException() {
        accountFrom.setBalance(BigDecimal.valueOf(50)); // Set balance lower than transaction amount
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);

        assertThatThrownBy(() -> transactionService.add(transactionRequestDTO))
                .isInstanceOf(IllegalStateException.class).hasMessage("Insufficient balance!");
    }

    @Test
    public void add_DeletedAccount_ThrowsInvalidDataAccessApiUsageException() {
        accountFrom.setIsDeleted(true); // Mark account as deleted
        when(transactionDTOMapper.toTransaction.apply(transactionRequestDTO)).thenReturn(validTransaction);

        assertThatThrownBy(() -> transactionService.add(transactionRequestDTO))
                .isInstanceOf(InvalidDataAccessApiUsageException.class).hasMessage("Invalid bank account provided!");
    }

}
