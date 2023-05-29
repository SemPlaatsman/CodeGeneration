package nl.inholland.codegeneration.models.DTO.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDTO(
    @NotNull(message = "IBAN from cannot be null!")
    String accountFromIban,
    @NotNull(message = "IBAN to cannot be null!")
    String accountToIban,
    @NotNull(message = "Amount cannot be null!")
    @Min(value = 0, message = "Amount cannot be lower than zero!")
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
