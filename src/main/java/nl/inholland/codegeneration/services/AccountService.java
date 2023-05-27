package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAll(QueryParams queryParams) {
        return accountRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }

    public List<Account> getAllByUserId(Long id) throws APIException {
        if(!userRepository.existsById(id)){
            throw new APIException("not users found", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        return accountRepository.findAllByUserId(id);
    }

    public Account insertAccount(Account account) throws APIException {
       if(account.getUser().getIsDeleted() ==true){
        throw new APIException("unauthorized", HttpStatus.UNAUTHORIZED, LocalDateTime.now());
       }
        return accountRepository.save(
            new Account(null, account.getAccountType(), account.getUser(),null, account.getAbsoluteLimit(),null)
            );
    }

    public Optional<Account> getAccountByIban(String iban) throws APIException {
            Optional<Account> account = accountRepository.findByIban(iban);
            if(account.isPresent()){
            return account;
            }else{
                throw new APIException("Account not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
            }
       
    }

    public Account updateAccount(Account account,String Iban) throws APIException {

            System.out.println(account.getIsDeleted());

            Optional<Account> _account = accountRepository.findByIban(Iban);
            if(account.getUser()==null){
                throw new APIException("Iban", HttpStatus.UNAUTHORIZED, LocalDateTime.now());
            }
            Optional<User> user =  userRepository.findById(account.getUser().getId());
          
            if (_account.isPresent()&&user.isPresent()) {
            
                _account.get().setUser(user.get());
                _account.get().setIban(Iban);
                _account.get().setAccountType(account.getAccountType());
                _account.get().setUser(account.getUser());
                _account.get().setBalance(account.getBalance());
                _account.get().setAbsoluteLimit(account.getAbsoluteLimit());


                return accountRepository.save(_account.get());
            } else {
                throw new APIException("Account not for this user", HttpStatus.UNAUTHORIZED, LocalDateTime.now());
            }

        

    }

    public void deleteAccount(String iban) throws APIException {
          Optional<Account> _account =  accountRepository.findByIban(iban);
            if(_account.isPresent()){
                accountRepository.delete(_account.get());
            }else{
                throw new APIException("account whit iban: "+iban+" not found", HttpStatus.NOT_FOUND, LocalDateTime.now());
            }
      
    }

    public List<Transaction> getTransactions(String accountID) throws APIException {
            List<Transaction> accounts =  transactionRepository.findAllByAccountFromIban(accountID);
            if(accounts.isEmpty()){
                throw new APIException("No transactions for "+accountID,HttpStatus.NOT_FOUND, LocalDateTime.now());
            }
            return accounts;

        
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