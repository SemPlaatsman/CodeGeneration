package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.exceptions.APIException;
import nl.inholland.codegeneration.models.DTO.request.UserUpdateRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.UserResponseDTO;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.models.DTO.request.UserRequestDTO;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.services.mappers.AccountDTOMapper;
import nl.inholland.codegeneration.services.mappers.UserDTOMapper;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    UserDTOMapper userDTOMapper;
    @InjectMocks
    UserService userService;
    @Mock
    private AccountDTOMapper accountDTOMapper;
    @Mock
    PasswordEncoder passwordEncoder;
  
    User AuthenticationUser = new User(null, null, null, null, null, null, null, null, null, null, null, null);

    @BeforeEach
    public void setup() {
        userDTOMapper = new UserDTOMapper(Mockito.mock(TransactionRepository.class));
        userDTOMapper.toUser = Mockito.mock(Function.class);
        userDTOMapper.toResponseDTO = Mockito.mock(Function.class);
        userDTOMapper.toUserFromUpdate = Mockito.mock(Function.class);


        userService = new UserService(userRepository, accountRepository, userDTOMapper, passwordEncoder);

        AuthenticationUser.setUsername("sarawilson");
        AuthenticationUser.setPassword("sara123");
        AuthenticationUser.setRoles(Collections.singletonList(Role.EMPLOYEE)); // Assuming the user has the role
                                                                               // "EMPLOYEE"

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                        AuthenticationUser,
                        "sara123",
                        AuthenticationUser.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

    }

 //TODO: Add tests for the getAll method
    @Test
    public void testGetAll() throws Exception {
       // Mock the necessary objects
       QueryParams<User> queryParams = mock(QueryParams.class);
       Specification<User> specification = mock(Specification.class);
       UserRepository userRepository = mock(UserRepository.class);
       UserDTOMapper userDTOMapper = mock(UserDTOMapper.class);
       Page<User> userPage = mock(Page.class);
       List<User> userList = new ArrayList<>();
       userList.add(new User());
       when(userPage.getContent()).thenReturn(userList);
       when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(userPage);
       when(userDTOMapper.toResponseDTO.apply(any(User.class))).thenReturn(new UserResponseDTO(any(User.class), any(BigDecimal.class)));

       // Create an instance of MyClass
       V myClass = new MyClass(userRepository, userDTOMapper);

       // Call the method under test
       List<UserResponseDTO> result = myClass.getAll(queryParams, true);

       // Assert the result
       assertEquals(1, result.size());

       // Verify the method calls
       verify(userRepository).findAll(any(Specification.class), any(PageRequest.class));
       verify(userDTOMapper, times(1)).toResponseDTO.apply(any(User.class));
    }


    @Test
    public void testGetById_whenUserExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.getById(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetById_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            userService.getById(1L);
        } catch (EntityNotFoundException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(List.of(1), "username", "password", "firstname", "lastname",
                "email@example.com", "1234567890", LocalDate.now());
        // ... set other fields as needed
        User user = new User();
        when(userDTOMapper.toUser.apply(userRequestDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.add(userRequestDTO);
        verify(userRepository).save(user);
    }

    @Test
    public void testUpdateUser() {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(1L, List.of(1), "username", "password", "firstname", "lastname",
                "email@example.com", "1234567890", LocalDate.now());
        // ... set other fields as needed
        User user = new User();
        user.setId(1L);
        user.setPassword("");
        when(userDTOMapper.toUserFromUpdate.apply(userUpdateRequestDTO)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.update(userUpdateRequestDTO, 1L);
        verify(userRepository).save(user);
    }

    @Test
    public void testUpdateUser_invalidId() {
        List<Integer> roles = new ArrayList<>();
        roles.add(1);
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(1L, List.of(1), "username", "password", "firstname", "lastname",
                "email@example.com", "1234567890", LocalDate.now());
        User user = new User();
        user.setId(1L);
        user.setPassword("");
        when(userDTOMapper.toUserFromUpdate.apply(userUpdateRequestDTO)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException.class, () ->  userService.update(userUpdateRequestDTO, 1L));

        assertEquals("User not found!", exception.getMessage());

    }

    @Test
    public void testDeleteUser() throws APIException {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.delete(1L);
        verify(userRepository).save(user);
    }
}