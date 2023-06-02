package nl.inholland.codegeneration.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();

        userDetails = User.withDefaultPasswordEncoder()
                .username("testUser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    public void testGenerateAndValidateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token, "Token should not be null");

        boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid, "Token should be valid");

        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username, "Extracted username should match original");
    }

    @Test
    public void testValidateTokenWithInvalidUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUserDetails = User.withDefaultPasswordEncoder()
                .username("otherUser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        boolean isValid = jwtService.validateToken(token, otherUserDetails);
        assertFalse(isValid, "Token should be invalid for different user");
    }
}