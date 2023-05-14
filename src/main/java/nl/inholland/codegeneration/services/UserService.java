package nl.inholland.codegeneration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll(@Nullable QueryParams queryParams) {
        return userRepository.findAll(PageRequest.of(queryParams.getPage(), queryParams.getLimit())).getContent();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User add(User user) {
        user.setId(null);
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
