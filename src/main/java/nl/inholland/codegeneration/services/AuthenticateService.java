package nl.inholland.codegeneration.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.security.AuthenticationRequest;
import nl.inholland.codegeneration.security.AuthenticationResponse;
import nl.inholland.codegeneration.security.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

  private final UserRepository userRepository;

  public AuthenticationResponse login(RegisterRequest request) {
    var user = User.builder()

    .build();
    return null;
  }

public AuthenticationResponse register(AuthenticationRequest request) {
    return null;
}


    
}
