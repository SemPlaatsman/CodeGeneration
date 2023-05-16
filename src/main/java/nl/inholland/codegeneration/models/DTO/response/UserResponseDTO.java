package nl.inholland.codegeneration.models.DTO.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(
    Long id,
    int role,
    String username,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthdate,
    BigDecimal dayLimit,
    BigDecimal transactionLimit
) {
}
