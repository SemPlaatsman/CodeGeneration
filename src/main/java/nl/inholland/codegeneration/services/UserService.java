package nl.inholland.codegeneration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
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


    //authentication thingies
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findOneByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

     

        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(user.getPassword())//
                .authorities(user.getRole().toString())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
        
    }
}
