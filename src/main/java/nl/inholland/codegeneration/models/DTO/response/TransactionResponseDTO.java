package nl.inholland.codegeneration.models.DTO.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
    Long id,
    LocalDateTime timestamp,
    String accountFromIban,
    String accountFromUsername,
    String accountToIban,
    String accountToUsername,
    BigDecimal amount,
    String description
) {
}
