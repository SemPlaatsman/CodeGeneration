package nl.inholland.codegeneration.models.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import nl.inholland.codegeneration.models.MinAge;

import java.time.LocalDate;
import java.util.List;

public record UserRequestDTO (
    @NotEmpty(message = "You need to include at least one role!")
    @Size(min = 1, max = 2, message = "You need to include at least one role!")
//    @PositiveOrZero(message = "All role ID's must be zero or above!")
    List<Integer> roles,
    @Schema(example = "testUser")
    @NotNull(message = "Username cannot be null!")
    @NotEmpty(message = "Username cannot be empty!")
    String username,
    @Schema(example = "test123")
    @NotNull(message = "Password cannot be null!")
    @NotEmpty(message = "Password cannot be empty!")
    String password,
    @Schema(example = "Test")
    @NotNull(message = "First name cannot be null!")
    @NotEmpty(message = "First name cannot be empty!")
    String firstName,
    @Schema(example = "User")
    @NotNull(message = "Last name cannot be null!")
    @NotEmpty(message = "Last name cannot be empty!")
    String lastName,
    @Schema(example = "test@user.dev")
    @NotNull(message = "Email cannot be null!")
    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    String email,
    @Schema(example = "06 12345678")
    @NotNull(message = "Phone number cannot be null!")
    @NotEmpty(message = "Phone number cannot be empty!")
    String phoneNumber,
    @Schema(example = "2001-01-01")
    @NotNull(message = "Birthdate cannot be null!")
    @MinAge(18)
    LocalDate birthdate
) {
    public UserRequestDTO(List<Integer> roles, String username, String password, String firstName, String lastName, String email, String phoneNumber, LocalDate birthdate) {
        this.roles = roles;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
    }
}
