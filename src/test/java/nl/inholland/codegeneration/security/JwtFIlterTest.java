package nl.inholland.codegeneration.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.inholland.codegeneration.services.JwtService;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest{

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtFilter jwtFilter;

  

    @BeforeEach
    public void setup() {
    }

    
    @Test
    public void testDoFilterInternal_WithValidToken_ShouldSetAuthenticationInSecurityContextHolder() throws ServletException, IOException {
        String jwt = "validToken";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(createUserDetails(username));
        when(jwtService.validateToken(jwt, createUserDetails(username))).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(createUserDetails(username));
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).extractUsername(jwt);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtService, times(1)).validateToken(jwt, createUserDetails(username));
        verify(authentication, times(1)).setDetails(any());
        verify(SecurityContextHolder.getContext(), times(1)).setAuthentication(authentication);
    }

    @Test
    public void testDoFilterInternal_WithInvalidToken_ShouldContinueFilterChain() throws ServletException, IOException {
        
        String jwt = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("sBearer " + jwt);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoMoreInteractions(SecurityContextHolder.getContext());
    }

    @Test
    public void testDoFilterInternal_WithMissingToken_ShouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        // verifyZeroInteractions(jwtService, userDetailsService, authentication);
        verifyNoMoreInteractions(SecurityContextHolder.getContext());
    }

   
 
}


