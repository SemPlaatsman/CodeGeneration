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

    // @Test
    // void testGetTransactionByIdWithMapper() {
    //     TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(1L, LocalDateTime.now(), "NL06INHO0000000001", "johndoe", "NL05INHO0000000002", "sarawilson", new BigDecimal("20"), "test description"); // Initialize this according to your requirements
    //     when(transactionRepository.findById(any(Long.class)))
    //         .thenReturn(Optional.of(transaction));
    //     when(transactionDTOMapper.toResponseDTO.apply(any(Transaction.class)))
    //         .thenReturn(transactionResponseDTO);

    //     assertEquals(transactionResponseDTO, transactionService.getById(1L));
    // }

    }




// package nl.inholland.codegeneration.services;

// import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
// import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
// import nl.inholland.codegeneration.models.Transaction;
// import nl.inholland.codegeneration.models.User;
// import nl.inholland.codegeneration.models.Account;
// import nl.inholland.codegeneration.models.AccountType;
// import nl.inholland.codegeneration.models.QueryParams;
// import nl.inholland.codegeneration.repositories.AccountRepository;
// import nl.inholland.codegeneration.repositories.TransactionRepository;
// import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;

// import jakarta.persistence.EntityNotFoundException;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Collections;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doReturn;
// import static org.mockito.Mockito.when;

// public class TransactionServiceTest {

//     @InjectMocks
//     private TransactionService transactionService;

//     @Mock
//     private TransactionRepository transactionRepository;

//     private TransactionDTOMapper transactionDTOMapper;

//     private TransactionResponseDTO transactionResponseDTO;

//     @Mock
//     private AccountRepository accountRepository;

//     private Transaction transaction;
//     private User user;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         // user = new User();
//         // user.setId(1L);

//         // transaction = new Transaction();
//         // transaction.setId(1L);
//         // transaction.setPerformingUser(user);
//         // transaction.setTimestamp(LocalDateTime.now());

//         this.transactionDTOMapper = new TransactionDTOMapper(accountRepository);
//         this.transactionResponseDTO = new TransactionResponseDTO(null, null, null, null, null, null, null, null);

//         // transactionDTOMapper = Mockito.mock(TransactionDTOMapper.class);
//         // when(transactionDTOMapper.toTransaction).thenReturn(request -> /* return the
//         // expected Transaction object */);
//         // when(transactionDTOMapper.toResponseDTO).thenReturn(transaction -> /* return
//         // the expected TransactionResponseDTO object */);
//         // transactionService = new TransactionService(transactionRepository,
//         // transactionDTOMapper);

//         // SecurityContextHolder.getContext().setAuthentication(new
//         // UsernamePasswordAuthenticationToken(user, null));
//     }

//     @Test
//     void testGetAllTransactions() {
//         QueryParams queryParams = new QueryParams();
//         queryParams.setPage(0);
//         queryParams.setLimit(5);

//         Specification<Transaction> spec = queryParams.buildFilter();
//         PageRequest pageable = PageRequest.of(queryParams.getPage(), queryParams.getLimit());

//         Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));

//         when(transactionRepository.findAll(spec, pageable)).thenReturn(page);

//         assertEquals(Collections.singletonList(transaction), transactionService.getAll(queryParams));
//     }

//     @Test
//     void testGetAllTransactionsWithEmptyPage() {
//         QueryParams queryParams = new QueryParams();
//         queryParams.setPage(0);
//         queryParams.setLimit(5);

//         Specification<Transaction> spec = queryParams.buildFilter();
//         PageRequest pageable = PageRequest.of(queryParams.getPage(), queryParams.getLimit());

//         Page<Transaction> page = new PageImpl<>(Collections.emptyList());

//         when(transactionRepository.findAll(spec, pageable)).thenReturn(page);

//         assertEquals(Collections.emptyList(), transactionService.getAll(queryParams));
//     }

//     @Test
//     void testGetById() {

//     }

    // @Test
    // void testGetTransactionById() {
    // Long testId = 1L;
    // User user = new User();
    // // Make sure to properly initialize user
    // user.setId(1L);
    // user.setUsername("Test User");

    // Account account = new Account(null, null, user, null, null, null);
    // // Make sure to properly initialize account
    // account.setUserById(1L);
    // account.setUser(user);

    // Transaction transaction = new Transaction(testId, null, account, account,
    // null, user, null);
    // // Make sure to properly initialize transaction
    // transaction.setPerformingUser(user);
    // transaction.setTimestamp(LocalDateTime.now());
    // TransactionResponseDTO transactionResponseDTO = new
    // TransactionResponseDTO(transaction); // Initialize this according to your
    // // requirements

    // when(transactionRepository.findById(testId)).thenReturn(Optional.of(transaction));

    // when(transactionDTOMapper.convertToResponseDTO(any(Transaction.class))).thenReturn(transactionResponseDTO);

    // TransactionResponseDTO retrievedTransaction =
    // transactionService.getById(testId);

    // assertEquals(transactionResponseDTO, retrievedTransaction);
    // }

    // @Test
    // public void testGetAccountByIban() throws APIException {
    // String testIban = "testIban";
    // User user = new User();
    // Account account = new Account("iban123", AccountType.CURRENT, user, new
    // BigDecimal("100.00"), new BigDecimal("500.00"), false);
    // when(accountRepository.findByIban(testIban)).thenReturn(Optional.of(account));

    // Optional<Account> retrievedAccount =
    // accountService.getAccountByIban(testIban);

    // assertTrue(retrievedAccount.isPresent());
    // assertEquals(account, retrievedAccount.get());
    // }

//     @Test
//     void testGetTransactionByIdNotFound() {
//         when(transactionRepository.findById(any(Long.class)))
//                 .thenReturn(Optional.empty());

//         assertThrows(EntityNotFoundException.class, () -> {
//             transactionService.getById(1L);
//         });
//     }

//     @Test
//     void testGetTransactionByIdWithMapper() {
//         TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(any()); // Initialize this according
//                                                                                            // to your requirements
//         when(transactionRepository.findById(any(Long.class)))
//                 .thenReturn(Optional.of(transaction));
//         when(transactionDTOMapper.toResponseDTO.apply(any(Transaction.class)))
//                 .thenReturn(transactionResponseDTO);

//         assertEquals(transactionResponseDTO, transactionService.getById(1L));
//     }

//     @Test
//     void testGetTransactionById() {
//         Long testId = 1L;
//     User user = new User();
//     // Make sure to properly initialize user
//     user.setId(1L);
//     user.setUsername("Test User");

//     Account account = new Account(null, null, user, null, null, null);
//     // Make sure to properly initialize account
//     account.setUserById(1L);
//     account.setUser(user);

//     Transaction transaction = new Transaction(testId, null, account, account,
//     null, user, null);
//     // Make sure to properly initialize transaction
//     transaction.setPerformingUser(user);
//     transaction.setTimestamp(LocalDateTime.now());
//     TransactionResponseDTO transactionResponseDTO = new
//     TransactionResponseDTO(transaction); // Initialize this according to your
//     // requirements

//         when(transactionRepository.findById(any(Long.class)))
//                 .thenReturn(Optional.of(transaction));
//         when(transactionDTOMapper.toResponseDTO.apply(transaction)).thenReturn(transactionResponseDTO);

//         assertEquals(transactionResponseDTO, transactionService.getById(1L));
//     }
// }
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