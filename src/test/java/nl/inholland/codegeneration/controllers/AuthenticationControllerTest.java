package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.configuration.apiTestConfiguration;
import nl.inholland.codegeneration.security.requests.AuthenticationRequest;
import nl.inholland.codegeneration.security.requests.RegisterRequest;
import nl.inholland.codegeneration.security.response.AuthenticationResponse;
import nl.inholland.codegeneration.services.AuthenticateService;
// import nl.inholland.codegeneration.services.TransactionService;
// import nl.inholland.codegeneration.services.UserService;
// import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.cucumber.core.gherkin.messages.internal.gherkin.internal.com.eclipsesource.json.Json;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@Import(apiTestConfiguration.class)
public class AuthenticationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationController accountController;

    @MockBean
    private AuthenticateService authenticateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    void testRegister() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("dummy_token");
    
        when(authenticateService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);
    
        // When & Then
        mockMvc.perform(post("/authenticate/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerRequest)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    void testLogin() throws Exception {
        // Given
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("john123");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("dummy_token");
    
        when(authenticateService.login(loginRequest)).thenReturn(expectedResponse);
    
        // When & Then
        mockMvc.perform(post("/authenticate/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());
    } 
}