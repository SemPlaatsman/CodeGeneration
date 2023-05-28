package nl.inholland.codegeneration.models.DTO.request;

import java.time.LocalDate;
import java.util.List;

public record UserRequestDTO(
    List<Integer> roles,
    String username,
    String password,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthdate
) {
    public UserRequestDTO(int role, String username, String password, String firstName, String lastName, String email, String phoneNumber, LocalDate birthdate) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
    }
}
