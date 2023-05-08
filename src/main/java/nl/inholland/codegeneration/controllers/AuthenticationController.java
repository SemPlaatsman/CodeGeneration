package nl.inholland.codegeneration.controllers;

import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.security.AuthenticationResponse;
import nl.inholland.codegeneration.security.RegisterRequest;
import nl.inholland.codegeneration.services.AuthenticateService;

@RestController
@RequestMapping(path = "/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateService authenticateService;



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticateService.login(request));
        
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticateService.register(request));
    }

}
