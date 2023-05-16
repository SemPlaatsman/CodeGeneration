package nl.inholland.codegeneration.models.DTO.request;

import java.math.BigDecimal;

public record TransactionRequestDTO(
    String accountFromIban,
    String accountToIban,
    BigDecimal amount,
    String description
) {
    public TransactionRequestDTO(String accountFromIban, String accountToIban, BigDecimal amount, String description) {
        this.accountFromIban = accountFromIban;
        this.accountToIban = accountToIban;
        this.amount = amount;
        this.description = (description != null) ? description : "";
    }
}
