package nl.inholland.codegeneration.models.DTO.response;

import nl.inholland.codegeneration.models.Transaction;

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
    public TransactionResponseDTO(Transaction transaction) {
        this(transaction.getId(), transaction.getTimestamp(), transaction.getAccountFrom().getIban(), transaction.getAccountFrom().getUser().getUsername(),
                transaction.getAccountTo().getIban(), transaction.getAccountTo().getUser().getUsername(), transaction.getAmount(), transaction.getDescription());
    }
}
