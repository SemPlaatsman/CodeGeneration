package nl.inholland.codegeneration.security.requests;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "testUser")
    @NotNull(message = "Please fill the username field!")
    @NotEmpty(message = "Please fill the username field!")
    private String username;

    @Schema(example = "test123")
    @NotNull(message = "Please fill the password field!")
    @NotEmpty(message = "Please fill the password field!")
    private String password;

    @Schema(example = "Test")
    @NotNull(message = "Please fill the first name field!")
    @NotEmpty(message = "Please fill the first name field!")
    private String firstName;

    @Schema(example = "User")
    @NotNull(message = "Please fill the last name field!")
    @NotEmpty(message = "Please fill the last name field!")
    private String lastName;

    @Schema(example = "test@user.dev")
    @NotNull(message = "Please fill the email field!")
    @NotEmpty(message = "Please fill the email field!")
    @Email(message = "Invalid email!")
    private String email;

    @Schema(example = "06 12345678")
    @NotNull(message = "Please fill the phone number field!")
    @NotEmpty(message = "Please fill the phone number field!")
    private String phoneNumber;

    @Schema(example = "2001-01-01")
    @NotNull(message = "Please fill the birth date field")
    @MinAge(18)
    private LocalDate birthdate;
}
