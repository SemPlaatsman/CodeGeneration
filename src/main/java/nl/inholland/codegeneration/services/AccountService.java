package nl.inholland.codegeneration.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.Account;

import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


 


    public List<Account> getAll() {
        System.out.println();
        return accountRepository.findAll();
    }


    public Account insertAccount(Account account) {

        System.out.println(account.getAbsoluteLimit());
        return accountRepository.save(new Account(account.getIban(), account.getAccountType(), account.getCustomer(),account.getBalance(), account.getAbsoluteLimit()));
    }

    public Optional<Account> getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    public Account updateAccount(Account account) {
        Account _account = accountRepository.findByIban(account.getIban()).get();
        _account.setIban(account.getIban());
        _account.setAccountType(account.getAccountType());
        _account.setCustomer(account.getCustomer());
        _account.setBalance(account.getBalance());
        _account.setAbsoluteLimit(account.getAbsoluteLimit());
        return accountRepository.save(_account);
    }

    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    } 

    public Optional<Account> getAccountTransaction(long accountID) {
        return accountRepository.findById(accountID);
    }

    public BigDecimal getAccountBalance(Long accountID) {
        return accountRepository.findById(accountID).get().getBalance();
    }


    

  


 

}