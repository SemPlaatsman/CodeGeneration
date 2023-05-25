package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccountByIban() {
        String testIban = "testIban";
        User user = new User();
        Account account = new Account("iban123", AccountType.CURRENT, user, new BigDecimal("100.00"), new BigDecimal("500.00"), false);
        when(accountRepository.findByIban(testIban)).thenReturn(Optional.of(account));

        Optional<Account> retrievedAccount = accountService.getAccountByIban(testIban);

        assertTrue(retrievedAccount.isPresent());
        assertEquals(account, retrievedAccount.get());
    }

    @Test
    public void testInsertAccount() {
        User user = new User();
        Account account = new Account("iban123", AccountType.CURRENT, user, new BigDecimal("100.00"), new BigDecimal("500.00"), false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.insertAccount(account);

        assertEquals(account, savedAccount);
        // Create an ArgumentCaptor for Account objects
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        // Verify that save was called once, and capture the argument
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        // Retrieve the captured account
        Account savedAccountArgument = accountCaptor.getValue();

        // Assert that the saved account has the properties you expect
        assertEquals("iban123", savedAccountArgument.getIban());
        assertEquals(AccountType.CURRENT, savedAccountArgument.getAccountType());
        assertEquals(user, savedAccountArgument.getUser());
        assertEquals(new BigDecimal("100.00"), savedAccountArgument.getBalance());
        assertEquals(new BigDecimal("500.00"), savedAccountArgument.getAbsoluteLimit());
        assertEquals(false, savedAccountArgument.getIsDeleted());
    }

    @Test
    void testUpdateAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        user.setId(1L);
        account.setUser(user);
        account.setIban("DE12500105170648489890");
        when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(account, "DE12500105170648489890");
        assertEquals(account, updatedAccount);
    }

    @Test
    void testDeleteAccount() {
        Account account = new Account();
        account.setIban("DE12500105170648489890");
        when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));

        assertDoesNotThrow(() -> accountService.deleteAccount("DE12500105170648489890"));
    }

    @Test
    void testGetTransactions() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAllByAccountFromIban(anyString())).thenReturn(transactions);

        List<Transaction> result = accountService.getTransactions("12345");
        assertEquals(transactions, result);
    }

    @Test
    void testGetBalance() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.00"));
        when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.getBalance("12345");
        assertEquals(new BigDecimal("1000.00"), balance);
    }
}