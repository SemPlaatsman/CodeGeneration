package nl.inholland.codegeneration.models.DTO.response;

import lombok.NoArgsConstructor;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDTO(
    Long id,
    List<Integer> roles,
    String username,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthdate,
    BigDecimal dayLimit,
    BigDecimal remainingDayLimit,
    BigDecimal transactionLimit
) {
    public UserResponseDTO(User user, BigDecimal remainingDayLimit) {
        this(user.getId(), user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), user.getBirthdate(), user.getDayLimit(), remainingDayLimit, user.getTransactionLimit());
    }
}
