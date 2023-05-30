package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetById() throws Exception {
        mockMvc.perform(get("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetAllAccountsById() throws Exception {
        mockMvc.perform(get("/users/{id}/accounts", 1).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testAdd() throws Exception {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        UserRequestDTO user = new UserRequestDTO(roles, "username", "password", "firstname", "lastname", "email@example.com", "1234567890", LocalDate.now());
        mockMvc.perform(post("/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testUpdate() throws Exception {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        // List<Role> roles = new ArrayList<>();
        // roles.add(Role.EMPLOYEE);
        UserRequestDTO user = new UserRequestDTO(roles, "username", "password", "firstname", "lastname", "email@example.com", "1234567890", LocalDate.now());
        mockMvc.perform(put("/users/{id}", 1)
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
    }
    
    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // register JavaTimeModule Dit geeft meer errors. Moet iets extra toevoegen.
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

// package nl.inholland.codegeneration.controllers;

// import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
// import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
// import nl.inholland.codegeneration.exceptions.APIException;
// import nl.inholland.codegeneration.models.Account;
// import nl.inholland.codegeneration.models.User;
// import nl.inholland.codegeneration.services.AccountService;
// import nl.inholland.codegeneration.services.UserService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
// import org.springframework.cglib.core.Local;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.TestExecutionListeners;
// import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// import javax.management.relation.Role;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.doNothing;

// @SpringBootTest
// @TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MockitoTestExecutionListener.class})
// public class UserControllerTest {

//     @InjectMocks
//     private UserController userController;

//     @Mock
//     private UserService userService;

//     @Mock
//     private AccountService accountService;

//     private UserRequestDTO userRequestDTO;
//     private UserResponseDTO userResponseDTO;
//     private Account account;

//     @BeforeEach
//     void setup() {
//         User user = new User();

//         List<Integer> roles = new ArrayList<>();
//         roles.add(1);

//         userRequestDTO = new UserRequestDTO(roles, "test", "test", "test", "test", "test", "test", LocalDate.now());
//         userResponseDTO = new UserResponseDTO(user);
//         account = new Account();
//     }

//     @Test
//     void testGetAll() {

//         when(userService.getAll(any())).thenReturn(Collections.singletonList(userResponseDTO));

//         try {
//             assertEquals(ResponseEntity.status(200).body(Collections.singletonList(userResponseDTO)),
//                     userController.getAll(null));
//         } catch (Exception e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }

//     @Test
//     void testGetById() {
//         when(userService.getById(anyLong())).thenReturn(userResponseDTO);

//         assertEquals(ResponseEntity.status(200).body(userResponseDTO),
//                 userController.getById(1L));
//     }

//     @Test
//     void testGetAllAccountsById() throws APIException {
//         when(accountService.getAllByUserId(anyLong())).thenReturn(Collections.singletonList(account));

//         assertEquals(ResponseEntity.status(200).body(Collections.singletonList(account)),
//                 userController.getAllAccountsById(1L));
//     }

//     @Test
//     void testAdd() {
//         when(userService.add(any())).thenReturn(userResponseDTO);

//         assertEquals(ResponseEntity.status(201).body(userResponseDTO),
//                 userController.add(userRequestDTO));
//     }

//     @Test
//     void testUpdate() {
//         when(userService.update(any(), anyLong())).thenReturn(userResponseDTO);

//         assertEquals(ResponseEntity.status(200).body(userResponseDTO),
//                 userController.update(userRequestDTO, 1L));
//     }

//     @Test
//     void testDelete() {
//         try {
//             doNothing().when(userService).delete(anyLong());
//         } catch (APIException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }

//         try {
//             assertEquals(ResponseEntity.status(204).body("No Content"),
//                     userController.delete(1L));
//         } catch (APIException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }
// }