package nl.inholland.codegeneration.controllers;

import java.util.List;
import jakarta.validation.Valid;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.FilterCriteria;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.UserService;
import javax.management.Query;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(@RequestParam(value = "filter", required = false) String filterQuery) throws Exception {
        QueryParams queryParams = new QueryParams(User.class);
        queryParams.setFilter(filterQuery);
        List<User> users = userService.getAll(queryParams);
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody User user) {
        User addedUser = userService.add(user);
        return ResponseEntity.status(201).body(addedUser);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody User user, @PathVariable Long id) {
        User updatedUser = userService.update(user, id);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(204).body("No Content");
    }
}