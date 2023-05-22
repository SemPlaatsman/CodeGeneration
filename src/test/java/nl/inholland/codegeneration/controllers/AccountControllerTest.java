package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.AccountService;
// import nl.inholland.codegeneration.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    // @MockBean
    // private JwtService jwtService;

    private String token;

    @BeforeEach
    void setUp() {
        // TODO: Replace this with actual logic to generate or get the token
        // this.token = "Bearer " + jwtService.createToken("Your User details");

        // If the token is static, you can directly assign it like this:
        // this.token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjg0Nzg0OTU5LCJleHAiOjE2ODQ4MjA5NTl9.Tcrz5wvxcAVmgudWcbVjbiDlMM2mRJSvvBjQDQEWp-Q";
    }

    @Test
    void getAllAccounts() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

        Account account2 = new Account();
        account2.setIban("NL01INHO0000000002");
        BigDecimal balance2 = new BigDecimal("1000.00");
        account2.setBalance(balance2);
        account2.setUserId(1L);

        when(accountService.getAll(any())).thenReturn(List.of(account, account2));

        mockMvc.perform(get("/accounts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void getAccountByIban() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

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
    void insertAccount() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

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
    void updateAccount() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

        when(accountService.updateAccount(any())).thenReturn(account);

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
    void deleteAccount() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

        when(accountService.deleteAccount("NL01INHO0000000001")).thenReturn(java.util.Optional.of(account));

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
    void getTransactions() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        transaction.setAmount(balance);
        transaction.setId(1L);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountTo("NL01INHO0000000002");
        BigDecimal balance2 = new BigDecimal("1000.00");
        transaction2.setAmount(balance2);
        transaction2.setId(1L);

        when(accountService.getTransactions(any())).thenReturn(List.of(transaction, transaction2));

        mockMvc.perform(get("/accounts/transactions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void getBalance() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        account.setUserId(1L);

        when(accountService.getBalance("NL01INHO0000000001")).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/accounts/balance/NL01INHO0000000001")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("NL01INHO0000000001"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.userId").value(1L))
                .andDo(print());
    }
    

}