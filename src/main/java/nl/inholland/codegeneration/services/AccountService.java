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

import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll(@Nullable QueryParams queryParams) {
        return accountRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }


    public Account insertAccount(Account account) {
        System.out.println(account.getAbsoluteLimit());
        return accountRepository.save(new Account(account.getIban(), account.getAccountType(), account.getCustomer(),account.getBalance(), account.getAbsoluteLimit()));
    }

    public Account getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    public Account updateAccount(Account account) {
        Account _account = accountRepository.findById(account.getIban()).get();
        _account.setIban(account.getIban());
        _account.setAccountType(account.getAccountType());
        _account.setCustomer(account.getCustomer());
        _account.setBalance(account.getBalance());
        _account.setAbsoluteLimit(account.getAbsoluteLimit());
        return accountRepository.save(_account);
    }

    public void deleteAccount(long id) {
//        accountRepository.deleteById(id);
    }

    public Optional<Account> getAccountTransaction(long accountID) {
//        return accountRepository.findById(accountID);
        return null;
    }

    public BigDecimal getAccountBalance(Long accountID) {
//        return accountRepository.findById(accountID).get().getBalance();
        return new BigDecimal(200);
    }


    

  


 

}