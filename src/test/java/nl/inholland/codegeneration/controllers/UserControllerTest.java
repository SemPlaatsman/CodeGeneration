package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.userdetails.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.PositiveOrZero;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testGetById() throws Exception {
        mockMvc.perform(get("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testGetAllAccountsById() throws Exception {
        mockMvc.perform(get("/users/{id}/accounts", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testAdd() throws Exception {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        UserRequestDTO user = new UserRequestDTO(roles, "username", "password", "firstname", "lastname",
                "email@example.com", "1234567890", LocalDate.now());
        mockMvc.perform(post("/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testUpdate() throws Exception {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        UserRequestDTO user = new UserRequestDTO(roles, "username", "password", "firstname", "lastname",
                "email@example.com", "1234567890", LocalDate.now());
        mockMvc.perform(put("/users/{id}", 1)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "EMPLOYEE" })
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // register JavaTimeModule Dit geeft meer errors. Moet iets
                                                         // extra toevoegen.
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

