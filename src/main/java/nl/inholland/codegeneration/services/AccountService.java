package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.inholland.codegeneration.models.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.DTO.request.AccountRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.BalanceResponseDTO;
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

    public List<AccountResponseDTO> getAll(QueryParams<Account> queryParams) throws Exception {
        return (List<AccountResponseDTO>) accountRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit()))
               .getContent().stream().map(AccountDTOMapper.toResponseDTO).collect(Collectors.toList());
    }

    public List<AccountResponseDTO> getAllByUserId(QueryParams<Account> queryParams, Long id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new APIException("User not found!", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        queryParams.addFilter(new FilterCriteria("user.id", ":", id));
        List<Account> accounts = accountRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
        // if (accounts.isEmpty()) {pStatus.NOT_FOUND, LocalDateTime.now());
        // }

        return (List<AccountResponseDTO>) accounts.stream().map(AccountDTOMapper.toResponseDTO).collect(Collectors.toList());
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

        //     throw new APIException("not accounts found", Htt
    public AccountResponseDTO getAccountByIban(String iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Account> account = accountRepository.findByIbanAndIsDeletedFalse(iban);
        if (account.isPresent()) {
            CustomerIbanCheck(user, account.get());
            return AccountDTOMapper.toResponseDTO.apply(account.get());
        } else {
            throw new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

    }

    public AccountResponseDTO updateAccount(AccountRequestDTO request, String Iban) throws APIException {

        Account account = AccountDTOMapper.toAccount.apply(request);

        if (account.getUser() == null) {
            throw new APIException("Unauthorized", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        Optional<Account> updatedAccount = accountRepository.findByIbanAndIsDeletedFalse(Iban);

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

        Optional<Account> addedAccount = accountRepository.findByIbanAndIsDeletedFalse(iban);
        if (addedAccount.isPresent()) {
            Account account = addedAccount.get();
            if(account.getIsDeleted())
            {
                throw new APIException("account whit iban: " + iban + " does not exist", HttpStatus.NOT_FOUND,
                LocalDateTime.now());
            }
            account.setIsDeleted(true);
            accountRepository.save(account);
        } else {
            throw new APIException("account whit iban: " + iban + " not found", HttpStatus.NOT_FOUND,
                    LocalDateTime.now());
        }

    }

    public List<TransactionResponseDTO> getTransactions(QueryParams<Transaction> queryParams, String iban) throws APIException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Transaction> accounts;
        if (user.getRoles().contains(Role.CUSTOMER)) {
              accounts = transactionRepository.findAllByAccountFromIbanAndUserId(iban, user.getId());
        }
        else {
            accounts = transactionRepository.findAllByAccountFromIban(iban);
        }

        if (accounts.isEmpty()) {
            throw new APIException("No transactions for " + iban, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return (List<TransactionResponseDTO>) transactionRepository.findAllByAccountFromIban(iban).stream()
                .map(TransactionDTOMapper.toResponseDTO).collect(Collectors.toList());
    }

    public BalanceResponseDTO getBalance(String iban) throws APIException {
        Optional<Account> account = accountRepository.findByIbanAndIsDeletedFalse(iban);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.isPresent()) {
            CustomerIbanCheck(user , account.get());
            Account updatedAccount = account.get();
            updatedAccount.setBalance(account.get().getBalance());
            return  AccountDTOMapper.toBalanceDTO.apply(updatedAccount);
        } else {
            throw new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

    }

    private Account CustomerIbanCheck(User user,  Account account) throws APIException {
       
        if (account.getUser() != user && user.getRoles().contains(Role.CUSTOMER)&& !user.getRoles().contains(Role.EMPLOYEE)) {
            throw new APIException("Forbidden!", HttpStatus.FORBIDDEN, LocalDateTime.now());
        }
        return account;
    }
}