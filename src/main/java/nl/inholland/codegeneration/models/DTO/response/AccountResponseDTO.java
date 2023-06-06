package nl.inholland.codegeneration.models.DTO.response;

import nl.inholland.codegeneration.models.Account;

import java.math.BigDecimal;

public record AccountResponseDTO(
    String iban,
    int accountType,
    String username,
    BigDecimal balance,
    BigDecimal absoluteLimit
) {
    public AccountResponseDTO(Account account) {
        this(account.getIban(), account.getAccountType().getValue(), account.getUser().getUsername(), account.getBalance(), account.getAbsoluteLimit());
    }

}
