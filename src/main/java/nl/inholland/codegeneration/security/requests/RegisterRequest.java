package nl.inholland.codegeneration.security.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.codegeneration.models.MinAge;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Please fill the username field!")
    @NotEmpty(message = "Please fill the username field!")
    private String username;

    @NotNull(message = "Please fill the password field!")
    @NotEmpty(message = "Please fill the password field!")
    private String password;

    @NotNull(message = "Please fill the first name field!")
    @NotEmpty(message = "Please fill the first name field!")
    private String firstName;

    @NotNull(message = "Please fill the last name field!")
    @NotEmpty(message = "Please fill the last name field!")
    private String lastName;

    @NotNull(message = "Please fill the email field!")
    @NotEmpty(message = "Please fill the email field!")
    @Email(message = "Invalid email!")
    private String email;

    @NotNull(message = "Please fill the phone number field!")
    @NotEmpty(message = "Please fill the phone number field!")
    private String phoneNumber;

    @NotNull(message = "Please fill the birth date field")
    @MinAge(18)
    private LocalDate birthdate;
}
