package nl.inholland.codegeneration.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtTokenProvider;

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest); // retrieve the token from the request
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) { // check if the token is valid
                Authentication auth = jwtTokenProvider.getAuthentication(token); // retrieve the user from the database
                SecurityContextHolder.getContext().setAuthentication(auth); // apply the user to the security context of
                                                                            // the request
            }
        } catch (ResponseStatusException ex) {
            SecurityContextHolder.clearContext(); // if the token is invalid, clear the security context
            httpServletResponse.sendError(ex.getStatusCode().value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse); // move on to the next filter
    }
}
