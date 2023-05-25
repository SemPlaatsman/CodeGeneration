package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.TransactionService;
import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
@Import(apiTestConfiguration.class)
public class TransactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    
    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    private String token;

    @BeforeEach
    void setUp() {
         // TODO: Replace this with actual logic to generate or get the token
        // this.token = "Bearer " + jwtService.createToken("Your User details");

        // If the token is static, you can directly assign it like this:
        this.token = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjg0Nzg0OTU5LCJleHAiOjE2ODQ4MjA5NTl9.Tcrz5wvxcAVmgudWcbVjbiDlMM2mRJSvvBjQDQEWp-Q";
    }

    @Test
    public void getAll() throws Exception {
        when(transactionService.getAll(null)).thenReturn(List.of(new Transaction(), new Transaction()));

        mockMvc.perform(get("/transactions")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getById() throws Exception {
        when(transactionService.getById(1)).thenReturn(new Transaction());

        mockMvc.perform(get("/transactions/1")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void add() throws Exception {
        Transaction transaction = new Transaction();
        BigDecimal amount = new BigDecimal(100);
        transaction.setAmount(amount);

        when(transactionService.add(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                .header(HttpHeaders.AUTHORIZATION, token)
                .content("{\"amount\": 100}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100));
    }
}