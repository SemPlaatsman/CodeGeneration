package nl.inholland.codegeneration.models.DTO.response;

import java.math.BigDecimal;

public record AccountResponseDTO(
    String iban,
    int accountType,
    String username,
    BigDecimal balance,
    BigDecimal absoluteLimit
) {
}
