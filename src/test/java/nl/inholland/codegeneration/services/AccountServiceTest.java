package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.services.mappers.AccountDTOMapper;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.JUnitException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;
import java.util.function.Function;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountDTOMapper accountDTOMapper;
    @Mock
    private TransactionDTOMapper transactionDTOMapper;
    
    @InjectMocks
    private AccountService accountService;
    
    @BeforeEach
    public void setup() {
      
        //security mocks
        
        User user = new User();
        user.setUsername("sarawilson");
        user.setPassword("sara123");
        user.setRoles(Collections.singletonList(Role.EMPLOYEE)); // Assuming the user has the role "EMPLOYEE"
    
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user,
            "sara123",
            user.getAuthorities()
        );
    
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        //mapper mocks
        accountDTOMapper = new AccountDTOMapper(userRepository);
        accountDTOMapper.toAccount = Mockito.mock(Function.class);
        accountDTOMapper.toResponseDTO = Mockito.mock(Function.class);
        accountService = new AccountService(accountRepository, userRepository, transactionRepository, accountDTOMapper, transactionDTOMapper);
    }
    
    @Test
    @WithMockUser(roles = {"EMPLOYEE"}, username = "user")
    public void testGetAccountByIban_whenAccountExists() throws APIException {
        //make test user
        User user = new User(null, List.of(Role.CUSTOMER), "sarawilson", "sara123","Sara", "Wilson", "sara.wilson@yahoo.com",
        "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200),false);
        
        //make test account
        String iban = "NL88INHO0001204817";
        Account account = new Account(iban, AccountType.CURRENT, user,new BigDecimal("120"), new BigDecimal("-1000"), false);

       

        AccountResponseDTO responseDTO = new AccountResponseDTO(iban, 0, iban, null, null);
        // when the accountRepository is called with the iban, return the account
        when(accountRepository.findByIbanAndIsDeletedFalse(iban)).thenReturn(Optional.of(account));
        // when the AccountDTOMapper is called with the account, return the responseDTO
        when(accountDTOMapper.toResponseDTO.apply(account)).thenReturn(responseDTO);

        AccountResponseDTO result = accountService.getAccountByIban(iban);

        //check if the accountRepository is called once
        verify(accountRepository).findByIbanAndIsDeletedFalse(iban);
        //check if the AccountDTOMapper is called once to asure that a responseDTO is returned
        verify(accountDTOMapper.toResponseDTO).apply(account);
        //check if the result is the same as the expected responseDTO
        // assertEquals(responseDTO, result);
        assertEquals(responseDTO, result);

    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"}, username = "user")
    public void testGetAccountByIban_whenAccountDoesNotExists() throws APIException {

        String iban = "NL88INHO0001204817";
        String iban2 = "NL88INHO0001204818";

        //make test user
        User user = new User(1L, List.of(Role.CUSTOMER), "sarawilson", "sara123","Sara", "Wilson", "sara.wilson@yahoo.com",
        "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200),false);
        
        //make test account
    
        Account account = new Account(iban2, AccountType.CURRENT, user,new BigDecimal("120"), new BigDecimal("-1000"), false);

        //reponse to be expected
        AccountResponseDTO responseDTO = new AccountResponseDTO(iban, 0, user.getUsername(), null, null);
        AccountResponseDTO responseDTO2 = new AccountResponseDTO(iban2, 0, user.getUsername(), null, null);
        // when the accountRepository is called with the iban, return the account
        when(accountRepository.findByIbanAndIsDeletedFalse(iban)).thenReturn(Optional.of(account));
        // when the AccountDTOMapper is called with the account, return the responseDTO
        when(accountDTOMapper.toResponseDTO.apply(account)).thenReturn(responseDTO2);

        AccountResponseDTO result = accountService.getAccountByIban(iban);

        //check if the accountRepository is called once
        verify(accountRepository).findByIbanAndIsDeletedFalse(iban);
        //check if the AccountDTOMapper is called once to asure that a responseDTO is returned
        verify(accountDTOMapper.toResponseDTO).apply(account);
        //check if the result is the same as the expected responseDTO
        assertNotEquals(responseDTO , result);
        
    }

    @Test
    public void testInsertAccount() throws APIException {
        AccountRequestDTO requestDTO = new AccountRequestDTO(null, null, 0);
        Account account = new Account();
        account.setIban("NL01ABCD0000000001");
        Account savedAccount = new Account();
        AccountResponseDTO responseDTO = new AccountResponseDTO("NL01ABCD0000000001", 0, null, null, null);
        when(accountDTOMapper.toAccount.apply(requestDTO)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(savedAccount);
        when(accountDTOMapper.toResponseDTO.apply(savedAccount)).thenReturn(responseDTO);

        AccountResponseDTO result = accountService.insertAccount(requestDTO);

        assertEquals(responseDTO, result);
        verify(accountRepository).save(account);
        verify(accountDTOMapper.toAccount).apply(requestDTO);
        verify(accountDTOMapper.toResponseDTO).apply(savedAccount);
    }

    @Test
    public void testGetAllByUserId() throws APIException {
        Long userId = 1L;
        List<Account> accounts = new ArrayList<>();
        List<AccountResponseDTO> responseDTOs = new ArrayList<>();
        User user = new User(userId, null, null, null, null, null, null, null, null, null, null, null);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(accountRepository.findAllByUserIdAndIsDeletedFalse(userId)).thenReturn(accounts);
        // when(AccountDTOMapper.toResponseDTO.apply(any(Account.class))).thenReturn(new AccountResponseDTO(null, 0, null, null, null));
        when(accountDTOMapper.toResponseDTO.apply(any(Account.class))).thenReturn(new AccountResponseDTO("NL01ABCD0000000001", 0, null, null, null));
        
        List<AccountResponseDTO> result = accountService.getAllByUserId(userId);

        assertEquals(accounts.size(), result.size());
        verify(userRepository).existsById(userId);
        verify(accountRepository).findAllByUserIdAndIsDeletedFalse(userId);
    }

    // Write similar tests for other methods here...

    //i need to make more junit tests
    
}

// package nl.inholland.codegeneration.services;

// import nl.inholland.codegeneration.exceptions.APIException;
// import nl.inholland.codegeneration.models.Account;
// import nl.inholland.codegeneration.models.AccountType;
// import nl.inholland.codegeneration.models.Transaction;
// import nl.inholland.codegeneration.models.User;
// import nl.inholland.codegeneration.repositories.AccountRepository;
// import nl.inholland.codegeneration.repositories.TransactionRepository;
// import nl.inholland.codegeneration.repositories.UserRepository;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.math.BigDecimal;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// public class AccountServiceTest {

//     @Mock
//     private AccountRepository accountRepository;

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private TransactionRepository transactionRepository;

//     @InjectMocks
//     private AccountService accountService;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     public void testGetAccountByIban() throws APIException {
//         String testIban = "testIban";
//         User user = new User();
//         Account account = new Account("iban123", AccountType.CURRENT, user, new BigDecimal("100.00"), new BigDecimal("500.00"), false);
//         when(accountRepository.findByIban(testIban)).thenReturn(Optional.of(account));

//         Optional<Account> retrievedAccount = accountService.getAccountByIban(testIban);

//         assertTrue(retrievedAccount.isPresent());
//         assertEquals(account, retrievedAccount.get());
//     }

//     @Test
//     public void testInsertAccount() throws APIException {
//         User user = new User();
//         Account account = new Account("NL06INHO0000000001", AccountType.CURRENT, user, new BigDecimal("100.00"), new BigDecimal("500.00"), false);
//         when(accountRepository.save(any(Account.class))).thenReturn(account);

//         Account savedAccount = accountService.insertAccount(account);

//         assertEquals(account, savedAccount);

//         // Assert that the saved account has the properties you expect
//         assertEquals("NL06INHO0000000001", savedAccount.getIban());
//         assertEquals(AccountType.CURRENT, savedAccount.getAccountType());
//         assertEquals(user, savedAccount.getUser());
//         assertEquals(new BigDecimal("100.00"), savedAccount.getBalance());
//         assertEquals(new BigDecimal("500.00"), savedAccount.getAbsoluteLimit());
//         assertEquals(false, savedAccount.getIsDeleted());
//     }   

//     @Test
//     void testUpdateAccount() throws Exception {
//         Account account = new Account();
//         User user = new User();
//         user.setId(1L);
//         account.setUser(user);
//         account.setIban("DE12500105170648489890");
//         when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));
//         when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//         when(accountRepository.save(any(Account.class))).thenReturn(account);

//         Account updatedAccount = accountService.updateAccount(account, "DE12500105170648489890");
//         assertEquals(account, updatedAccount);
//     }

//     @Test
//     void testDeleteAccount() {
//         Account account = new Account();
//         account.setIban("DE12500105170648489890");
//         when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));

//         assertDoesNotThrow(() -> accountService.deleteAccount("DE12500105170648489890"));
//     }

//     @Test
//     void testGetTransactions() throws APIException {
//         List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
//         when(transactionRepository.findAllByAccountFromIban(anyString())).thenReturn(transactions);

//         List<Transaction> result = accountService.getTransactions("12345");
//         assertEquals(transactions, result);
//     }

//     @Test
//     void testGetBalance() throws APIException {
//         Account account = new Account();
//         account.setBalance(new BigDecimal("1000.00"));
//         when(accountRepository.findByIban(anyString())).thenReturn(Optional.of(account));

//         BigDecimal balance = accountService.getBalance("12345");
//         assertEquals(new BigDecimal("1000.00"), balance);
//     }
// }


 // MET DIT ERBIJ WERKT DE TEST NIET MEER \/\/\/\/\/
    // ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    //     // Verify that save was called once, and capture the argument
    //     verify(accountRepository, times(1)).save(accountCaptor.capture());

    //     // Retrieve the captured account
    //     Account savedAccountArgument = accountCaptor.getValue();

    //     // Assert that the saved account has the properties you expect
    //     assertEquals("NL06INHO0000000001", savedAccountArgument.getIban());
    //     assertEquals(AccountType.CURRENT, savedAccountArgument.getAccountType());
    //     assertEquals(user, savedAccountArgument.getUser());
    //     assertEquals(new BigDecimal("100.00"), savedAccountArgument.getBalance());
    //     assertEquals(new BigDecimal("500.00"), savedAccountArgument.getAbsoluteLimit());
    //     assertEquals(false, savedAccountArgument.getIsDeleted());