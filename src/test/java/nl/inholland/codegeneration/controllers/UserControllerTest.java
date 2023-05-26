package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.UserService;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.JwtService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.JwtService;
import nl.inholland.codegeneration.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(apiTestConfiguration.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        // this.token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjg0Nzg0OTU5LCJleHAiOjE2ODQ4MjA5NTl9.Tcrz5wvxcAVmgudWcbVjbiDlMM2mRJSvvBjQDQEWp-Q";
    }

    @Test
    public void getAll() throws Exception {
        when(userService.getAll(null)).thenReturn(List.of(new User(), new User()));

        mockMvc.perform(get("/users")
                .header(HttpHeaders.AUTHORIZATION, this.token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        when(userService.getById(1L)).thenReturn(new User());

        mockMvc.perform(get("/users/1")
                .header(HttpHeaders.AUTHORIZATION, this.token))
                .andDo(print());
    }

    @Test
    public void add() throws Exception {
        when(userService.add(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .content("{}"))
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        when(userService.update(any(User.class), any(Long.class))).thenReturn(new User());

        mockMvc.perform(post("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .content("{}"))
                .andDo(print());
    }
    
    @Test
    public void delete() throws Exception {
        mockMvc.perform(post("/users/1/delete")
                .header(HttpHeaders.AUTHORIZATION, this.token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}