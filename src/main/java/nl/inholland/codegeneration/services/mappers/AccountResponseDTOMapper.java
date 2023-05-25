package nl.inholland.codegeneration.services.mappers;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AccountResponseDTOMapper implements Function<Account, AccountResponseDTO> {
    @Override
    public AccountResponseDTO apply(Account account) {
        return new AccountResponseDTO(account);
    }
}
