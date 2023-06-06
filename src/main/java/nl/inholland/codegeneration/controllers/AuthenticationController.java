package nl.inholland.codegeneration.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.security.requests.AuthenticationRequest;
import nl.inholland.codegeneration.security.requests.RegisterRequest;
import nl.inholland.codegeneration.security.response.AuthenticationResponse;
import nl.inholland.codegeneration.services.AuthenticateService;

@RestController
@RequestMapping(path = "/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticateService authenticateService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authenticateService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateService.login(request));
        
    }

}
