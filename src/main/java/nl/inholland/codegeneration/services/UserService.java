package nl.inholland.codegeneration.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.services.mappers.UserResponseDTOMapper;
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
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserResponseDTOMapper userResponseDTOMapper;

    public List<UserResponseDTO> getAll(@Nullable QueryParams queryParams) {
        return (List<UserResponseDTO>) userRepository.findAll(queryParams.buildFilter(), PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent().stream().map(userResponseDTOMapper).collect(Collectors.toList());
    }

    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
        return userResponseDTOMapper.apply(user);
    }

    public UserResponseDTO add(User user) {
        user.setId(null);
        user.setIsDeleted(false);
        return userResponseDTOMapper.apply(userRepository.save(user));
    }

    public UserResponseDTO update(User user, Long id) {
        if (user.getId() != id) {
            throw new InvalidDataAccessApiUsageException("Invalid id!");
        }
        User existingUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
        existingUser.update(user);
        return userResponseDTOMapper.apply(userRepository.save(existingUser));
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) throws APIException {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
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
