package nl.inholland.codegeneration.controllers;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.FilterCriteria;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.FilterSpecification;
import nl.inholland.codegeneration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.management.Query;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin("http://localhost:5173/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(@Valid QueryParams queryParams) {
        try {
            List<User> users = userService.getAll(queryParams);
            return ResponseEntity.status(200).body(users);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            return ResponseEntity.status(200).body(user);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody User user) {
        try {
            User addedUser = userService.add(user);
            return ResponseEntity.status(201).body(addedUser);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody User user, @PathVariable Long id) {
        try {
            User updatedUser = userService.update(user, id);
            return ResponseEntity.status(200).body(updatedUser);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(204).body("No Content");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}