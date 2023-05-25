package nl.inholland.codegeneration.services.mappers;

import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.models.User;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class UserResponseDTOMapper implements Function<User, UserResponseDTO> {
    @Override
    public UserResponseDTO apply(User user) {
        return new UserResponseDTO(user);
    }
}
