package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.status(200).body(users);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> insertUser(@RequestBody User user) {
        User _user = userRepository.save(new User(null, false, true, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getBirthdate(), user.getDayLimit(), user.getTransactionLimit(), false));
        return ResponseEntity.status(201).body(_user);
    }
}
