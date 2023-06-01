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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionDTOMapper transactionDTOMapper;

    private Transaction transaction;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setPerformingUser(user);
        transaction.setTimestamp(LocalDateTime.now());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    void testGetAllTransactions() {
        QueryParams queryParams = new QueryParams();
        queryParams.setPage(0);
        queryParams.setLimit(5);

        Specification<Transaction> spec = queryParams.buildFilter();
        PageRequest pageable = PageRequest.of(queryParams.getPage(), queryParams.getLimit());

        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));

        when(transactionRepository.findAll(spec, pageable)).thenReturn(page);

        assertEquals(Collections.singletonList(transaction), transactionService.getAll(queryParams));
    }

    @Test
    void testGetAllTransactionsWithEmptyPage() {
        QueryParams queryParams = new QueryParams();
        queryParams.setPage(0);
        queryParams.setLimit(5);

        Specification<Transaction> spec = queryParams.buildFilter();
        PageRequest pageable = PageRequest.of(queryParams.getPage(), queryParams.getLimit());

        Page<Transaction> page = new PageImpl<>(Collections.emptyList());

        when(transactionRepository.findAll(spec, pageable)).thenReturn(page);

        assertEquals(Collections.emptyList(), transactionService.getAll(queryParams));
    }

    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(transaction));

        assertEquals(transaction, transactionService.getById(1L));
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            transactionService.getById(1L);
        });
    }

    @Test
void testGetTransactionByIdWithMapper() {
    TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(any()); // Initialize this according to your requirements
    when(transactionRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(transaction));
    when(transactionDTOMapper.toResponseDTO.apply(any(Transaction.class)))
            .thenReturn(transactionResponseDTO);

    assertEquals(transactionResponseDTO, transactionService.getById(1L));
}

}
// @Test
// void testAddTransaction() {
// Transaction newTransaction = new Transaction();
// newTransaction.setPerformingUser(user);
// newTransaction.setTimestamp(LocalDateTime.now());

// when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);

// Transaction addedTransaction = transactionService.add(newTransaction);

// assertEquals(newTransaction, addedTransaction);
// }

// public class TransactionServiceTest {

// @InjectMocks
// private TransactionService transactionService;

// @Mock
// private TransactionRepository transactionRepository;

// @Mock
// private TransactionDTOMapper transactionDTOMapper;

// @Mock
// private TransactionResponseDTO transactionResponseDTO;

// private Transaction transaction;
// private User user;
// private Account accountFrom;
// private Account accountTo;
// private QueryParams queryParams;

// @BeforeEach
// public void setUp() {
// user = new User();
// user.setTransactionLimit(BigDecimal.valueOf(1000));

// accountFrom = new Account();
// accountFrom.setUser(user);
// accountFrom.setIsDeleted(false);
// accountFrom.setBalance(BigDecimal.valueOf(2000));
// accountFrom.setAbsoluteLimit(BigDecimal.valueOf(500));
// accountFrom.setAccountType(AccountType.CURRENT);

// accountTo = new Account();
// accountTo.setUser(user);
// accountTo.setIsDeleted(false);

// transaction = new Transaction();
// transaction.setAccountFrom(accountFrom);
// transaction.setAccountTo(accountTo);
// transaction.setAmount(BigDecimal.valueOf(500));

// SecurityContextHolder.getContext().setAuthentication(new
// UsernamePasswordAuthenticationToken(user, null));

// queryParams = new QueryParams();
// queryParams.setPage(0);
// queryParams.setLimit(5);
// }
// @Test
// void testGetAllTransactions() {
// when(transactionRepository.findAll())
// .thenReturn(Collections.singletonList(transaction));
// when(transactionDTOMapper.toResponseDTO.apply(transaction)).thenReturn(transactionResponseDTO);

// assertEquals(Collections.singletonList(transactionResponseDTO),
// transactionService.getAll(null));
// }

// @Test
// void testGetTransactionById() {
// when(transactionRepository.findById(any(Long.class)))
// .thenReturn(Optional.of(transaction));
// when(transactionDTOMapper.toResponseDTO.apply(transaction)).thenReturn(transactionResponseDTO);

// assertEquals(transactionResponseDTO, transactionService.getById(1L));
// }

// @Test
// public void testGetAll() {
// when(transactionRepository.findAll(any(),
// any())).thenReturn(Arrays.asList(transaction));
// List<Transaction> transactions = transactionService.getAll(queryParams);
// assertNotNull(transactions);
// assertEquals(1, transactions.size());
// }

// @Test
// public void testGetById() {
// when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
// Transaction foundTransaction = transactionService.getById(1L);
// assertNotNull(foundTransaction);
// }

// @Test
// public void testAdd() {
// when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
// when(transactionRepository.findDailyTransactionsValueOfUser(anyLong())).thenReturn(BigDecimal.ZERO);
// Transaction addedTransaction = transactionService.add(transaction);
// assertNotNull(addedTransaction);
// }
// }