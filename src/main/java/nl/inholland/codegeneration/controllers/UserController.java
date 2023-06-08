package nl.inholland.codegeneration.controllers;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.AccountResponseDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDTO>> getAll(@RequestParam(value = "filter", required = false) String filterQuery,
                                                        @RequestParam(value = "limit", required = false) Integer limit,
                                                        @RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "hasAccount", required = false) Boolean hasAccount) throws Exception {
        QueryParams<User> queryParams = new QueryParams(User.class, limit, page);
        queryParams.setFilter(filterQuery);
        List<UserResponseDTO> users = userService.getAll(queryParams, hasAccount);
        return ResponseEntity.status(200).body(users);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        UserResponseDTO user = userService.getById(id);
        return ResponseEntity.status(200).body(user);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR ((hasAuthority('CUSTOMER') AND #id == authentication.principal.id))")
    @GetMapping(path = "/{id}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsById(@RequestParam(value = "filter", required = false) String filterQuery,
                                                @RequestParam(value = "limit", required = false) Integer limit,
                                                @RequestParam(value = "page", required = false) Integer page,
                                                @PathVariable Long id) throws Exception {
        QueryParams<Account> queryParams = new QueryParams(Account.class, limit, page);
        queryParams.setFilter(filterQuery);
        List<AccountResponseDTO> accounts = accountService.getAllByUserId(queryParams, id);
        return ResponseEntity.status(200).body(accounts);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> add(@RequestBody @Valid UserRequestDTO user) {
        UserResponseDTO addedUser = userService.add(user);
        return ResponseEntity.status(201).body(addedUser);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UserRequestDTO user, @PathVariable Long id) {
        UserResponseDTO updatedUser = userService.update(user, id);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws APIException {
        userService.delete(id);
        return ResponseEntity.status(204).body("No Content");
    }
}