package nl.inholland.codegeneration.services.mappers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper {
    private TransactionRepository transactionRepository;

    @Autowired
    public UserDTOMapper(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Function<UserRequestDTO, User> toUser = (userRequestDTO) -> {
        User user = new User();
        user.setRoles(userRequestDTO.roles().stream().map(Role::fromInt).collect(Collectors.toList()));
        user.setUsername(userRequestDTO.username());
        user.setPassword(userRequestDTO.password());
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setEmail(userRequestDTO.email());
        user.setPhoneNumber(userRequestDTO.phoneNumber());
        user.setBirthdate(userRequestDTO.birthdate());
        return user;
    };

    public Function<User, UserResponseDTO> toResponseDTO = (user) -> new UserResponseDTO(user, user.getDayLimit().subtract(this.transactionRepository.findDailyTransactionsValueOfUser(user.getId()).orElse(new BigDecimal(0))));
}
