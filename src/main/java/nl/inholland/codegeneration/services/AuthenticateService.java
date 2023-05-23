package nl.inholland.codegeneration.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.security.requests.AuthenticationRequest;
import nl.inholland.codegeneration.security.requests.RegisterRequest;
import nl.inholland.codegeneration.security.response.AuthenticationResponse;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setBirthdate(request.getBirthdate());

    //registering user is by default customer van change this if you want to register a new employee you could make a diferent endpoint for that
    user.setRole(Role.CUSTOMER);
    userRepository.save(user);

    String jwtToken =  jwtService.generateToken(user);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  public AuthenticationResponse login(AuthenticationRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
    String jwtToken =  jwtService.generateToken(user);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }
}
