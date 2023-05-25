package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public List<Account> getAllByUserId(Long id) {
        return accountRepository.findAllByUserId(id);
    }

    public Account insertAccount(Account account) {
        return accountRepository.save(new Account(account.getIban(), account.getAccountType(), account.getUser(),
                account.getBalance(), account.getAbsoluteLimit(), null));
    }

    public Optional<Account> getAccountByIban(String iban) {
        try {
            Optional<Account> account = accountRepository.findByIban(iban);
            return account;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Account updateAccount(Account account,String Iban) throws Exception {


        try {
            Optional<Account> _account = accountRepository.findByIban(Iban);
            if(account.getUser()==null){
                throw new Exception();
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
                throw new Exception("Account not found");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public void deleteAccount(String iban) {
        try {
          Optional<Account> _account =  accountRepository.findByIban(iban);
            if(_account.isPresent()){
                accountRepository.delete(_account.get());
            }else{
                throw new Exception("Account not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Transaction> getTransactions(String accountID) {
        try {
            List<Transaction> accounts =  transactionRepository.findAllByAccountFromIban(accountID);
            return accounts;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public BigDecimal getBalance(String accountID) {

        try {
            Optional<Account> account = accountRepository.findByIban(accountID);
            if (account.isPresent()) {
                return account.get().getBalance();
            } else {
                throw new Exception("Account not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}