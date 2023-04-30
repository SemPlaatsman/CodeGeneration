package nl.inholland.codegeneration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.security.JwtProvider;

public class AuthenticateService {

    AuthenticationManager authenticationManager;
    JwtProvider JwtProvider;

    @Autowired
    UserRepository userRepository;

    //returns a jwt token for a user
    public String Authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return JwtProvider.createToken(username, userRepository.findOneByUsername(username).get().getRole());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password");        
        }

    }
}
