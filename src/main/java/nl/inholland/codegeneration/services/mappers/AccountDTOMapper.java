package nl.inholland.codegeneration.services.mappers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AccountDTOMapper {
    @Autowired
    UserRepository userRepository;
    public Function<AccountRequestDTO, Account> toAccount = (accountRequestDTO) -> {
        Account account = new Account();
        account.setUser(userRepository.findById(accountRequestDTO.customerId()).orElseThrow(() -> new EntityNotFoundException("User not found!")));
        account.setAbsoluteLimit(accountRequestDTO.absoluteLimit());
        account.setAccountType(AccountType.fromInt(accountRequestDTO.accountType()));
        return account;
    };

    public Function<Account, AccountResponseDTO> toResponseDTO = AccountResponseDTO::new;
}
