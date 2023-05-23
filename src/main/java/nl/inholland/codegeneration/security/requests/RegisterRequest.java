package nl.inholland.codegeneration.security.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
 
    private LocalDate birthdate;
}
