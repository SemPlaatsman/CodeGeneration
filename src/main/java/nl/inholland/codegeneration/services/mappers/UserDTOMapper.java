package nl.inholland.codegeneration.services.mappers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper {
    public Function<UserRequestDTO, User> toUser = (userRequestDTO) -> {
        User user = new User();
        user.setRole(Role.fromInt(userRequestDTO.role()));
        user.setUsername(userRequestDTO.username());
        user.setPassword(userRequestDTO.password());
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setEmail(userRequestDTO.email());
        user.setPhoneNumber(userRequestDTO.phoneNumber());
        user.setBirthdate(userRequestDTO.birthdate());
        return user;
    };

    public Function<User, UserResponseDTO> toResponseDTO = UserResponseDTO::new;
}
