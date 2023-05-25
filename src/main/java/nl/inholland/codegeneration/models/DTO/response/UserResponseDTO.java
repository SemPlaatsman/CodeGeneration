package nl.inholland.codegeneration.models.DTO.response;

import lombok.NoArgsConstructor;
import nl.inholland.codegeneration.models.User;

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
    public UserResponseDTO(User user) {
        this(user.getId(), user.getRole().getValue(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), user.getBirthdate(), user.getDayLimit(), user.getTransactionLimit());
    }
}
