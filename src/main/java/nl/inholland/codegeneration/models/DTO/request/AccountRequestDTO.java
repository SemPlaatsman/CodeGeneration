package nl.inholland.codegeneration.models.DTO.request;

import java.math.BigDecimal;

//@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountRequestDTO(
    Long customerId,
    BigDecimal absoluteLimit,
    int accountType
) {
    public AccountRequestDTO(Long customerId, BigDecimal absoluteLimit, int accountType) {
        this.customerId = customerId;
        this.absoluteLimit = (absoluteLimit != null) ? absoluteLimit : new BigDecimal(0);
        this.accountType = accountType;
    }
}
