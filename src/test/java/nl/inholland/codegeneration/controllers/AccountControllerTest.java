package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
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

import javax.security.auth.x500.X500Principal;

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
        mockMvc.perform(get("/accounts/{iban}", "NL06INHO0000000001"))
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})//400 response
    public void testInsertAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        user.setRoles(List.of(Role.CUSTOMER));
        account.setUser(user);

        AccountRequestDTO requestDTO = new AccountRequestDTO(1L, null, 0);
        AccountResponseDTO responseDTO = new AccountResponseDTO("NL06INHO0000000001", 0, "Luuk", null, null);

        when(accountService.insertAccount(requestDTO)).thenReturn(responseDTO);
        mockMvc.perform(post("/accounts", account)
        .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})//400 response
    public void testUpdateAccount() throws Exception {
        Account account = new Account();
        User user = new User();
        user.setRoles(List.of(Role.EMPLOYEE));
        account.setUser(user);

        AccountRequestDTO requestDTO = new AccountRequestDTO(1L, null, 0);
        AccountResponseDTO responseDTO = new AccountResponseDTO("NL06INHO0000000001", 0, "Luuk", null, null);

        when(accountService.updateAccount(requestDTO, "NL06INHO0000000001")).thenReturn(responseDTO);
        mockMvc.perform(put("/accounts", requestDTO).contentType(MediaType.APPLICATION_JSON)
        // mockMvc.perform(put("/accounts").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
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