package nl.inholland.codegeneration.services.mappers;

import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionResponseDTOMapper implements Function<Transaction, TransactionResponseDTO> {
    @Override
    public TransactionResponseDTO apply(Transaction transaction) {
        return new TransactionResponseDTO(transaction);
    }
}
