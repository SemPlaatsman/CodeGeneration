package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.models.*;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

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

    // @Test
    // public void testGetAll() {
    //     when(transactionRepository.findAll(any(), any())).thenReturn(Arrays.asList(transaction));
    //     List<Transaction> transactions = transactionService.getAll(queryParams);
    //     assertNotNull(transactions);
    //     assertEquals(1, transactions.size());
    // }

    @Test
    public void testGetById() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        Transaction foundTransaction = transactionService.getById(1L);
        assertNotNull(foundTransaction);
    }

    @Test
    public void testAdd() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionRepository.findDailyTransactionsValueOfUser(anyLong())).thenReturn(BigDecimal.ZERO);
        Transaction addedTransaction = transactionService.add(transaction);
        assertNotNull(addedTransaction);
    }
}