package nl.inholland.codegeneration.services;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.exceptions.APIException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nl.inholland.codegeneration.models.FilterCriteria;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

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
        User existingUser = this.getById(id);
        existingUser.update(user);
        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        User user = this.getById(id);
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
