package nl.inholland.codegeneration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
//        FilterSpecification<User> spec = new FilterSpecification<>(new FilterCriteria("lastName", ":", "Doe"));
        return (List<User>)userRepository.findAll();
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
