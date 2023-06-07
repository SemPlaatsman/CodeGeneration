package nl.inholland.codegeneration.security.response;

import java.util.List;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.codegeneration.models.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String token;
    List<Integer> roles;
    String username;
    String email;
}
