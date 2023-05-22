package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll(null)).thenReturn(List.of(new User(), new User()));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getById() throws Exception {
        when(userService.getById(1L)).thenReturn(new User());

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void add() throws Exception {
        when(userService.add(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        when(userService.update(any(User.class), any(Long.class))).thenReturn(new User());

        mockMvc.perform(post("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/users/1/delete"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}