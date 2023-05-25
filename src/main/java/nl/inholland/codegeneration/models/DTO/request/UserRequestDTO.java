package nl.inholland.codegeneration.models.DTO.request;

import java.time.LocalDate;

public record UserRequestDTO(
    int role,
    String username,
    String password,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthdate
) {
}
