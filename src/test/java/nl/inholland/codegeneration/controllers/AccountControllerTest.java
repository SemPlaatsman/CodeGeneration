package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.SecurityConfig;
import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import(apiTestConfiguration.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    private String token;

    @BeforeEach
    void setUp() {
        // TODO: Replace this with actual logic to generate or get the token
        // this.token = "Bearer " + jwtService.createToken("Your User details");

        // If the token is static, you can directly assign it like this:
        // this.token =
        // "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjg0Nzg0OTU5LCJleHAiOjE2ODQ4MjA5NTl9.Tcrz5wvxcAVmgudWcbVjbiDlMM2mRJSvvBjQDQEWp-Q";
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void getAllAccounts() throws Exception {
        Account account = new Account();
        User user = new User();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUser(user);

        when(accountService.getAll(any())).thenReturn(List.of(account));

        Account account2 = new Account();
        account2.setIban("NL01INHO0000000002");
        BigDecimal balance2 = new BigDecimal("1000.00");
        account2.setBalance(balance2);
        account2.setUser(user);

        when(accountService.getAll(any())).thenReturn(List.of(account, account2));

        mockMvc.perform(get("/accounts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void getAccountByIban() throws Exception {
        Account account = new Account();
        User user = new User();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUser(user);

        when(accountService.getAccountByIban("NL01INHO0000000001")).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/accounts/NL01INHO0000000001")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("NL01INHO0000000001"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.userId").value(1L))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void insertAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUser(user);

        when(accountService.insertAccount(any())).thenReturn(account);

        mockMvc.perform(post("/accounts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"iban\": \"NL01INHO0000000001\",\n" +
                        "    \"balance\": 1000.00,\n" +
                        "    \"userId\": 1\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("NL01INHO0000000001"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.userId").value(1L))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void updateAccount() throws Exception {
    // Setup
    Account account = new Account();
    User user = new User();
    user.setId(1L); // assuming User has setId method
    account.setIban("NL01INHO0000000001");
    account.setUser(user);
    BigDecimal balance = new BigDecimal("1000.00");
    account.setBalance(balance);
    account.setAbsoluteLimit(balance); // assuming you have a similar limit
    // account.setAccountType(); You might need to set the account type here based on your implementation

    Account updatedAccount = new Account(); // The account returned by the service after update
    updatedAccount.setIban(account.getIban());
    updatedAccount.setUser(account.getUser());
    updatedAccount.setBalance(account.getBalance());
    updatedAccount.setAbsoluteLimit(account.getAbsoluteLimit());
    // updatedAccount.setAccountType(account.getAccountType());

    // Mocking the service
    when(accountService.updateAccount(any(Account.class), eq(account.getIban()))).thenReturn(updatedAccount);

    // Converting account to JSON
    ObjectMapper mapper = new ObjectMapper();
    String accountJson = mapper.writeValueAsString(account);

    // The test
    mockMvc.perform(put("/accounts") // Updating an account should be a PUT or PATCH request, not a POST
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(accountJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.iban").value("NL01INHO0000000001"))
            .andExpect(jsonPath("$.balance").value(1000.00))
            .andExpect(jsonPath("$.userId").value(1L)) // Assuming the returned Account has user's ID in the "userId" field
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void deleteAccount() throws Exception {
        String iban = "NL01INHO0000000001";

        // Assuming account exists
        doNothing().when(accountService).deleteAccount(iban);

        mockMvc.perform(delete("/accounts/" + iban)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        verify(accountService, times(1)).deleteAccount(iban);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void getTransactions() throws Exception {
        // Create accounts
        Account accountFrom = new Account();
        accountFrom.setIban("NL01INHO0000000001");

        Account accountTo = new Account();
        accountTo.setIban("NL01INHO0000000002");

        // Create user
        User user = new User(); // Set user properties if necessary

        // Create transactions
        Transaction transaction1 = new Transaction();
        transaction1.setAccountFrom(accountFrom);
        transaction1.setAmount(new BigDecimal("1000.00"));
        transaction1.setId(1L);
        transaction1.setAccountTo(accountTo);
        transaction1.setPerformingUser(user); // Assuming a user performs the transaction
        transaction1.setDescription("Transaction 1"); // A description for the transaction
        transaction1.setTimestamp(LocalDateTime.now()); // Current time as transaction timestamp

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom(accountFrom);
        transaction2.setAmount(new BigDecimal("1000.00"));
        transaction2.setId(2L); // Assuming it has a different ID
        transaction2.setAccountTo(accountTo);
        transaction2.setPerformingUser(user);
        transaction2.setDescription("Transaction 2");
        transaction2.setTimestamp(LocalDateTime.now());

        // Stub the service
        when(accountService.getTransactions(any())).thenReturn(List.of(transaction1, transaction2));

        // The test
        mockMvc.perform(get("/accounts/transactions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    void getBalance() throws Exception {
    Account account = new Account();
    User user = new User();
    account.setIban("NL01INHO0000000001");
    BigDecimal balance = new BigDecimal("1000.00");
    account.setBalance(balance);
    account.setUser(user);

    when(accountService.getBalance("NL01INHO0000000001")).thenReturn(balance);

    mockMvc.perform(get("/accounts/balance/NL01INHO0000000001")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"balance\": 1000.00}"))
            .andDo(print());
}


}