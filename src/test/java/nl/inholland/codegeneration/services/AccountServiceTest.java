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
import java.util.stream.Collectors;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

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

        // security mocks

        User user = new User();
        user.setUsername("sarawilson");
        user.setPassword("sara123");
        user.setRoles(Collections.singletonList(Role.EMPLOYEE)); // Assuming the user has the role "EMPLOYEE"

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                "sara123",
                user.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // mapper mocks
        accountDTOMapper = new AccountDTOMapper(userRepository);
        accountDTOMapper.toAccount = Mockito.mock(Function.class);
        accountDTOMapper.toResponseDTO = Mockito.mock(Function.class);
        accountService = new AccountService(accountRepository, userRepository, transactionRepository, accountDTOMapper,
                transactionDTOMapper);
    }

    @Test
    @WithMockUser(roles = { "EMPLOYEE" }, username = "user")
    public void testGetAccountByIban_whenAccountExists() throws APIException {
        // make test user
        User user = new User(null, List.of(Role.CUSTOMER), "sarawilson", "sara123", "Sara", "Wilson",
                "sara.wilson@yahoo.com",
                "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false);

        // make test account
        String iban = "NL88INHO0001204817";
        Account account = new Account(iban, AccountType.CURRENT, user, new BigDecimal("120"), new BigDecimal("-1000"),
                false);

        AccountResponseDTO responseDTO = new AccountResponseDTO(iban, 0, iban, null, null);
        // when the accountRepository is called with the iban, return the account
        when(accountRepository.findByIbanAndIsDeletedFalse(iban)).thenReturn(Optional.of(account));
        // when the AccountDTOMapper is called with the account, return the responseDTO
        when(accountDTOMapper.toResponseDTO.apply(account)).thenReturn(responseDTO);

        AccountResponseDTO result = accountService.getAccountByIban(iban);

        // check if the accountRepository is called once
        verify(accountRepository).findByIbanAndIsDeletedFalse(iban);
        // check if the AccountDTOMapper is called once to asure that a responseDTO is
        // returned
        verify(accountDTOMapper.toResponseDTO).apply(account);
        // check if the result is the same as the expected responseDTO
        // assertEquals(responseDTO, result);
        assertEquals(responseDTO, result);

    }

    @Test
    @WithMockUser(roles = { "EMPLOYEE" }, username = "user")
    public void testGetAccountByIban_whenAccountDoesNotExists() throws APIException {

        String iban = "NL88INHO0001204817";
        String iban2 = "NL88INHO0001204818";

        // make test user
        User user = new User(1L, List.of(Role.CUSTOMER), "sarawilson", "sara123", "Sara", "Wilson",
                "sara.wilson@yahoo.com",
                "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false);

        // make test account

        Account account = new Account(iban2, AccountType.CURRENT, user, new BigDecimal("120"), new BigDecimal("-1000"),
                false);

        // reponse to be expected
        AccountResponseDTO responseDTO = new AccountResponseDTO(iban, 0, user.getUsername(), null, null);
        AccountResponseDTO responseDTO2 = new AccountResponseDTO(iban2, 0, user.getUsername(), null, null);
        // when the accountRepository is called with the iban, return the account
        when(accountRepository.findByIbanAndIsDeletedFalse(iban)).thenReturn(Optional.of(account));
        // when the AccountDTOMapper is called with the account, return the responseDTO
        when(accountDTOMapper.toResponseDTO.apply(account)).thenReturn(responseDTO2);

        AccountResponseDTO result = accountService.getAccountByIban(iban);

        // check if the accountRepository is called once
        verify(accountRepository).findByIbanAndIsDeletedFalse(iban);
        // check if the AccountDTOMapper is called once to asure that a responseDTO is
        // returned
        verify(accountDTOMapper.toResponseDTO).apply(account);
        // check if the result is the same as the expected responseDTO
        assertNotEquals(responseDTO, result);

    }

    @Test
    @WithMockUser(roles = { "EMPLOYEE" }, username = "user")
    public void testInsertAccount() throws APIException {


        String iban = "NL88INHO0001204817";
        // make test user
        User user = new User(1L, List.of(Role.CUSTOMER), "sarawilson", "sara123", "Sara", "Wilson",
                "sara.wilson@yahoo.com",
                "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false);
        AccountRequestDTO requestDTO = new AccountRequestDTO(user.getId(), new BigDecimal("5000"),
                AccountType.SAVINGS.getValue());
        Account addedAccount = new Account();
        addedAccount.setIban(iban);
        addedAccount.setAccountType(AccountType.SAVINGS);
        addedAccount.setUser(user);
        addedAccount.setAbsoluteLimit(new BigDecimal("5000"));
        addedAccount.setBalance(new BigDecimal("0"));
        addedAccount.setIsDeleted(false);

        AccountResponseDTO expectedResponse = new AccountResponseDTO(addedAccount);

        when(accountRepository.save(any(Account.class))).thenReturn(addedAccount);
        when(accountDTOMapper.toResponseDTO.apply(addedAccount)).thenReturn(expectedResponse);
        when(accountDTOMapper.toAccount.apply(requestDTO)).thenReturn(addedAccount);
        AccountResponseDTO result = accountService.insertAccount(requestDTO);


        // Verify the result
        assertNotNull(result);
        assertEquals(iban, result.iban());
        assertEquals(AccountType.SAVINGS.getValue(), result.accountType());
        assertEquals(user.getUsername(), result.username());
        assertEquals(new BigDecimal("0"), result.balance());
        assertEquals(new BigDecimal("5000"), result.absoluteLimit());

        // Verify the method calls
        verify(accountRepository).save(any(Account.class));
    }

   
    @Test
    public void testGetAllByUserId() throws Exception {
        Long userId = 1L;
        User user = new User(userId, List.of(Role.CUSTOMER), "sarawilson", "sara123", "Sara", "Wilson","sara.wilson@yahoo.com",
                "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false);
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("NL88INHO0001204817", AccountType.CURRENT, user, new BigDecimal("120"), new BigDecimal("-1000"), false));
        accounts.add(new Account("NL44INHO0003204819", AccountType.SAVINGS, user, new BigDecimal("500"),new BigDecimal("0"), false));

        List<AccountResponseDTO> expectedResponse = accounts.stream().map(AccountResponseDTO::new).collect(Collectors.toList());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(accountRepository.findAllByUserIdAndIsDeletedFalse(userId)).thenReturn(accounts);
        when((List<AccountResponseDTO>) accounts.stream().map(AccountResponseDTO::new).collect(Collectors.toList())).thenReturn(accounts.stream().map(AccountResponseDTO::new).collect(Collectors.toList()));

        List<AccountResponseDTO> result = accountService.getAllByUserId( userId);

        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());
        assertEquals(expectedResponse, result);

        verify(userRepository).existsById(userId);
        verify(accountRepository).findAllByUserIdAndIsDeletedFalse(userId);
    }

    @Test
    void testDeleteAccount() {
        
    }

    @Test
    void testGetAll() {
        
    }


    @Test
    void testGetBalance() {
        
    }

    @Test
    void testGetTransactions() {
        
    }

    @Test
    void testUpdateAccount() {
        
    }

    

 

}
