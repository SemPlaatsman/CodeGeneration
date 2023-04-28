package nl.inholland.codegeneration.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import nl.inholland.codegeneration.services.UserService;

@Component
public class JwtProvider {
    //generate a jwt token provider class for a user
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
  
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h
  
    @Autowired
    private UserService user;
  
    @PostConstruct
    protected void init() {
      secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
  
    public String createToken(String username, List<Role> appUserRoles) {
  
      Claims claims = Jwts.claims().setSubject(username);
      claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(((GrantedAuthority) s).getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
  
      Date now = new Date();
      Date validity = new Date(now.getTime() + validityInMilliseconds);
  
      return Jwts.builder()//
          .setClaims(claims)//
          .setIssuedAt(now)//
          .setExpiration(validity)//
          .signWith(SignatureAlgorithm.HS256, secretKey)//
          .compact();
    }
  
    public Authentication getAuthentication(String token) {
      UserDetails UserDetails = user.loadUserByUsername(getUsername(token));
      return new UsernamePasswordAuthenticationToken(user, "", UserDetails.getAuthorities());
    }
  
    public String getUsername(String token) {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
  
    public String resolveToken(HttpServletRequest req) {
      String bearerToken = req.getHeader("Authorization");
      if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
      }
      return null;
    }
  
    public boolean validateToken(String token) throws ResponseStatusException {
      try {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
      } catch (JwtException | IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Expired or invalid JWT token");
      }
    }

}
