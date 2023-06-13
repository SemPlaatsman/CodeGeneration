package nl.inholland.codegeneration.models.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDTO (
    @Schema(example = "NL06INHO0000000001")
    @NotNull(message = "IBAN from cannot be null!")
    @NotEmpty(message = "IBAN from cannot be empty!")
    String accountFromIban,
    @Schema(example = "NL07INHO0000000002")
    @NotNull(message = "IBAN to cannot be null!")
    @NotEmpty(message = "IBAN to cannot be empty!")
    String accountToIban,
    @Schema(example = "20")
    @NotNull(message = "Amount cannot be null!")
    @Min(value = 0, message = "Amount cannot be lower than zero!")
    BigDecimal amount,
    @Schema(example = "Example description")
    String description
) {
    public TransactionRequestDTO(String accountFromIban, String accountToIban, BigDecimal amount, String description) {
        this.accountFromIban = accountFromIban;
        this.accountToIban = accountToIban;
        this.amount = amount;
        this.description = (description != null) ? description : "";
    }
}
