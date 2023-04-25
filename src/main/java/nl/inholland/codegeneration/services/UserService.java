package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
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
        User existingUser = getById(user.getId());
        existingUser.update(user);
        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        userRepository.delete(getById(id));
    }
}
