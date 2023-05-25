package nl.inholland.codegeneration.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<User> getAll(@Nullable QueryParams queryParams) {
        return userRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }

    public User getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new EntityNotFoundException("User not found!");
        return user;
    }

    public User add(User user) {
        user.setId(null);
        user.setIsDeleted(false);
        return userRepository.save(user);
    }

    public User update(User user, Long id) {
        if (user.getId() != id) {
            throw new InvalidDataAccessApiUsageException("Invalid id!");
        }
        User existingUser = this.getById(id);
        existingUser.update(user);
        return userRepository.save(existingUser);
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) throws APIException {
        User user = this.getById(id);
        user.setIsDeleted(true);
        User deletedUser = userRepository.save(user);
        List<Account> accounts = accountRepository.findAllByUserId(id);
        accounts.forEach(account -> account.setIsDeleted(true));
        List<Account> deletedAccounts = accountRepository.saveAll(accounts);
        if (!deletedUser.getIsDeleted() || deletedAccounts.stream().anyMatch(account -> !account.getIsDeleted())) {
            throw new APIException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        }
    }
}
