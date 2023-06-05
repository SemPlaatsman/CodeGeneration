package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.AuthenticateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@Import(apiTestConfiguration.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @MockBean
    private AuthenticateService authenticateService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetAll() throws Exception {
        when(accountService.getAll(null)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/accounts")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetAccountByIban() throws Exception {
        // Account account = new Account();
        // User user = new User();
        // user.setRoles(List.of(Role.EMPLOYEE));
        // account.setUser(user);
        mockMvc.perform(get("/accounts/NL06INHO0000000001"))
        .andExpect(status().isOk());

        // when(accountService.getAccountByIban("NL06INHO0000000001")).thenReturn(Optional.of(account));
        // mockMvc.perform(get("/accounts/NL06INHO0000000001")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testInsertAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        user.setRoles(List.of(Role.CUSTOMER));
        account.setUser(user);

        when(accountService.insertAccount(account)).thenReturn(account);
        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testUpdateAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        user.setRoles(List.of(Role.EMPLOYEE));
        account.setUser(user);

        when(accountService.updateAccount(account, "NL06INHO0000000001")).thenReturn(account);
        mockMvc.perform(put("/accounts/NL06INHO0000000001").contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/accounts/NL01INHO0000000001")).andExpect(status().isNoContent());
    }

    @Test
//     @WithMockUser(username = "user", roles = {"CUSTOMER"})
    public void testGetTransactions() throws Exception {
        when(accountService.getTransactions("NL01INHO0000000001")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/accounts/NL01INHO0000000001/transactions")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    public void testGetBalance() throws Exception {
        when(accountService.getBalance("NL01INHO0000000001")).thenReturn(null);
        mockMvc.perform(get("/accounts/NL01INHO0000000001/balance")).andExpect(status().isOk());
    }
}