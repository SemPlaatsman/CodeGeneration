package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MockitoTestExecutionListener.class})
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private Account account;

    @BeforeEach
    void setup() {
        User user = new User();
        userRequestDTO = new UserRequestDTO(List.of(1), "test", "test", "test", "test", "test", "test", LocalDate.now());
        userResponseDTO = new UserResponseDTO(user);
        account = new Account();
    }

    @Test
    void testGetAll() {
        when(userService.getAll(any())).thenReturn(Collections.singletonList(userResponseDTO));

        try {
            assertEquals(ResponseEntity.status(200).body(Collections.singletonList(userResponseDTO)),
                    userController.getAll(null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void testGetById() {
        when(userService.getById(anyLong())).thenReturn(userResponseDTO);

        assertEquals(ResponseEntity.status(200).body(userResponseDTO),
                userController.getById(1L));
    }

    @Test
    void testGetAllAccountsById() {
        when(accountService.getAllByUserId(anyLong())).thenReturn(Collections.singletonList(account));

        assertEquals(ResponseEntity.status(200).body(Collections.singletonList(account)),
                userController.getAllAccountsById(1L));
    }

    @Test
    void testAdd() {
        when(userService.add(any())).thenReturn(userResponseDTO);

        assertEquals(ResponseEntity.status(201).body(userResponseDTO),
                userController.add(userRequestDTO));
    }

    @Test
    void testUpdate() {
        when(userService.update(any(), anyLong())).thenReturn(userResponseDTO);

        assertEquals(ResponseEntity.status(200).body(userResponseDTO),
                userController.update(userRequestDTO, 1L));
    }

    @Test
    void testDelete() {
        try {
            doNothing().when(userService).delete(anyLong());
        } catch (APIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            assertEquals(ResponseEntity.status(204).body("No Content"),
                    userController.delete(1L));
        } catch (APIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}