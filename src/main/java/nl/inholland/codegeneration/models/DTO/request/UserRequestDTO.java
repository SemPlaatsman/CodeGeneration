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
}
