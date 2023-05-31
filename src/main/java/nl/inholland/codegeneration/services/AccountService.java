package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.el.stream.Stream;
import org.apiguardian.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.services.mappers.AccountDTOMapper;
import nl.inholland.codegeneration.services.mappers.TransactionDTOMapper;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountDTOMapper AccountDTOMapper;
    private final TransactionDTOMapper TransactionDTOMapper;

    public List<Account> getAll(QueryParams queryParams) {
        return accountRepository
                .findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit()))
                .getContent();
    }

    public List<Account> getAllByUserId(Long id) throws APIException {
        if (!userRepository.existsById(id)) {
            throw new APIException("not users found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        return accountRepository.findAllByUserId(id);
    }

    public AccountResponseDTO insertAccount(AccountRequestDTO request) throws APIException {
        Account account = AccountDTOMapper.toAccount.apply(request);
        if (account.getUser().getIsDeleted() == true) {
            throw new APIException("unauthorized", HttpStatus.UNAUTHORIZED, LocalDateTime.now());
        }
        Account addedAccount = new Account();
        addedAccount.setAccountType(account.getAccountType());
        addedAccount.setUser(account.getUser());
        addedAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        return AccountDTOMapper.toResponseDTO.apply(accountRepository.save(addedAccount));
    }

    public Optional<Account> getAccountByIban(String iban) throws APIException {
        Optional<Account> account = accountRepository.findByIban(iban);
        if (account.isPresent()) {
            return account;
        } else {
            throw new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

    }

    public AccountResponseDTO updateAccount(AccountRequestDTO request, String Iban) throws APIException {

        Account account = AccountDTOMapper.toAccount.apply(request);

        if (account.getUser() == null) {
            throw new APIException("Unauthorized", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        Optional<Account> updatedAccount = accountRepository.findByIban(Iban);

        Optional<User> user = userRepository.findById(account.getUser().getId());

        if (updatedAccount.isPresent() && user.isPresent()) {

            updatedAccount.get().setAccountType(account.getAccountType());
            updatedAccount.get().setUser(account.getUser());
            updatedAccount.get().setAbsoluteLimit(account.getAbsoluteLimit());

            return AccountDTOMapper.toResponseDTO.apply(accountRepository.save(updatedAccount.get()));
        } else {
            throw new APIException("Account not for this user", HttpStatus.UNAUTHORIZED, LocalDateTime.now());
        }

    }

    public void deleteAccount(String iban) throws APIException {
        Optional<Account> addedAccount = accountRepository.findByIban(iban);
        if (addedAccount.isPresent()) {
            accountRepository.delete(addedAccount.get());
        } else {
            throw new APIException("account whit iban: " + iban + " not found", HttpStatus.NOT_FOUND,
                    LocalDateTime.now());
        }

    }

    public List<TransactionResponseDTO> getTransactions(String accountID) throws APIException {
        List<Transaction> accounts = transactionRepository.findAllByAccountFromIban(accountID);
        if (accounts.isEmpty()) {
            throw new APIException("No transactions for " + accountID, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return (List<TransactionResponseDTO>) transactionRepository.findAllByAccountFromIban(accountID).stream()
                .map(TransactionDTOMapper.toResponseDTO).collect(Collectors.toList());

    }

    public BigDecimal getBalance(String accountID) throws APIException {

        Optional<Account> account = accountRepository.findByIban(accountID);
        if (account.isPresent()) {
            return account.get().getBalance();
        } else {
            throw new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

    }
}