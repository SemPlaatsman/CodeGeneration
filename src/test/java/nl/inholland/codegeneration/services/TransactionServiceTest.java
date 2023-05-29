package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionDTOMapper transactionDTOMapper;

    @Mock
    private TransactionResponseDTO transactionResponseDTO;

    private Transaction transaction;
    private User user;
    private Account accountFrom;
    private Account accountTo;
    private QueryParams queryParams;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setTransactionLimit(BigDecimal.valueOf(1000));

        accountFrom = new Account();
        accountFrom.setUser(user);
        accountFrom.setIsDeleted(false);
        accountFrom.setBalance(BigDecimal.valueOf(2000));
        accountFrom.setAbsoluteLimit(BigDecimal.valueOf(500));
        accountFrom.setAccountType(AccountType.CURRENT);

        accountTo = new Account();
        accountTo.setUser(user);
        accountTo.setIsDeleted(false);

        transaction = new Transaction();
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setAmount(BigDecimal.valueOf(500));

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));

        queryParams = new QueryParams();
        queryParams.setPage(0);
        queryParams.setLimit(5);
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll())
                .thenReturn(Collections.singletonList(transaction));
        when(transactionDTOMapper.toResponseDTO.apply(transaction)).thenReturn(transactionResponseDTO);

        assertEquals(Collections.singletonList(transactionResponseDTO),
                transactionService.getAll(null));
    }

    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(transaction));
        when(transactionDTOMapper.toResponseDTO.apply(transaction)).thenReturn(transactionResponseDTO);

        assertEquals(transactionResponseDTO, transactionService.getById(1L));
    }

    // @Test
    // public void testGetAll() {
    //     when(transactionRepository.findAll(any(), any())).thenReturn(Arrays.asList(transaction));
    //     List<Transaction> transactions = transactionService.getAll(queryParams);
    //     assertNotNull(transactions);
    //     assertEquals(1, transactions.size());
    // }

    // @Test
    // public void testGetById() {
    //     when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
    //     Transaction foundTransaction = transactionService.getById(1L);
    //     assertNotNull(foundTransaction);
    // }

    // @Test
    // public void testAdd() {
    //     when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
    //     when(transactionRepository.findDailyTransactionsValueOfUser(anyLong())).thenReturn(BigDecimal.ZERO);
    //     Transaction addedTransaction = transactionService.add(transaction);
    //     assertNotNull(addedTransaction);
    // }
}