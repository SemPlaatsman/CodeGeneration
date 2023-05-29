package nl.inholland.codegeneration.models.DTO.request;

import jakarta.validation.constraints.*;
import nl.inholland.codegeneration.models.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record UserRequestDTO(
    @NotEmpty(message = "You need to include at least one role!")
    @Size(min = 1, message = "You need to include at least one role!")
    @PositiveOrZero(message = "All role ID's must be zero or above!")
    List<Integer> roles,
    @NotNull(message = "Username cannot be null!")
    String username,
    @NotNull(message = "Password cannot be null!")
    String password,
    @NotNull(message = "First name cannot be null!")
    String firstName,
    @NotNull(message = "Last name cannot be null!")
    String lastName,
    @NotNull(message = "Email cannot be null!")
    @Email(message = "Invalid email address!")
    String email,
    @NotNull(message = "Phone number cannot be null!")
    String phoneNumber,
    @NotNull(message = "Birthdate cannot be null!")
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
