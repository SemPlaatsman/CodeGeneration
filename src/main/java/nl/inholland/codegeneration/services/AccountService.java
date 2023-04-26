package nl.inholland.codegeneration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.repositories.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account insertAccount(Account account) {
        return accountRepository.save(new Account(0, account.getIban(), account.getAccountType(), account.getCustomer(),account.getBalance(), account.getAbsoluteLimit()));
    }
}