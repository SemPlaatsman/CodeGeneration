package nl.inholland.codegeneration.models.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

//@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountRequestDTO(
    @NotNull(message = "Customer id cannot be null!")
    Long customerId,
    @NotNull(message = "Absolute limit cannot be null!")
    @Min(value = 0, message = "Absolute limit cannot be lower than zero!")
    BigDecimal absoluteLimit,
    @NotNull(message = "Account type cannot be null!")
    @Min(value = 0, message = "Account type cannot be lower than zero!")
    int accountType
) {
    public AccountRequestDTO(Long customerId, BigDecimal absoluteLimit, int accountType) {
        this.customerId = customerId;
        this.absoluteLimit = (absoluteLimit != null) ? absoluteLimit : new BigDecimal(0);
        this.accountType = accountType;
    }
}
