package nl.inholland.codegeneration.controllers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.configuration.WebConfig;
import nl.inholland.codegeneration.exceptions.APIExceptionHandler;
import nl.inholland.codegeneration.models.DTO.request.UserUpdateRequestDTO;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.userdetails.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.PositiveOrZero;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, accountService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new APIExceptionHandler())
                .build();
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
    public void testGetByInvalidId() throws Exception {
        Long invalidId = -1L;

        when(userService.getById(invalidId)).thenThrow(new EntityNotFoundException("User not found!"));

        mockMvc.perform(get("/users/{id}", invalidId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("User not found!", result.getResolvedException().getMessage()));
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
        UserRequestDTO user = new UserRequestDTO(List.of(1), "username", "password", "firstname", "lastname",
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
        UserUpdateRequestDTO user = new UserUpdateRequestDTO(1L, roles, "username", "password", "firstname", "lastname",
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


