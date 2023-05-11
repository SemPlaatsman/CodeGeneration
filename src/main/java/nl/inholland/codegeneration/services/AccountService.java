package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import nl.inholland.codegeneration.models.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.Account;
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
    private UserRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAll() {
        System.out.println();
        return accountRepository.findAll();

    }

    public Account insertAccount(Account account) {

        return accountRepository.save(new Account(account.getIban(), account.getAccountType(), account.getCustomer(),
                account.getBalance(), account.getAbsoluteLimit()));

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
            Optional<User> user =  customerRepository.findById(account.getCustomer().getId());
            if (_account.isPresent()&&user.isPresent()) {
            
                _account.get().setCustomer(user.get());
                _account.get().setIban(Iban);
                _account.get().setAccountType(account.getAccountType());
                _account.get().setCustomer(account.getCustomer());
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