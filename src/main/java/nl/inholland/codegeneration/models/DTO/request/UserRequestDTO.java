package nl.inholland.codegeneration.models.DTO.request;

import jakarta.validation.constraints.*;
import nl.inholland.codegeneration.models.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record UserRequestDTO (
    @NotEmpty(message = "You need to include at least one role!")
    @Size(min = 1, max = 2, message = "You need to include at least one role!")
//    @PositiveOrZero(message = "All role ID's must be zero or above!")
    List<Integer> roles,
    @NotNull(message = "Username cannot be null!")
    @NotEmpty(message = "Username cannot be empty!")
    String username,
    @NotNull(message = "Password cannot be null!")
    @NotEmpty(message = "Password cannot be empty!")
    String password,
    @NotNull(message = "First name cannot be null!")
    @NotEmpty(message = "First name cannot be empty!")
    String firstName,
    @NotNull(message = "Last name cannot be null!")
    @NotEmpty(message = "Last name cannot be empty!")
    String lastName,
    @NotNull(message = "Email cannot be null!")
    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    String email,
    @NotNull(message = "Phone number cannot be null!")
    @NotEmpty(message = "Phone number cannot be empty!")
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
