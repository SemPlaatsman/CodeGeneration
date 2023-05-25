package nl.inholland.codegeneration.models.DTO.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserRequestDTOTest {

    @Test
    public void testConstructor_setsFieldsCorrectly() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@test.com";
        String phoneNumber = "1234567890";
        LocalDate birthdate = LocalDate.now();

        // When
        UserRequestDTO dto = new UserRequestDTO(username, password, firstName, lastName, email, phoneNumber, birthdate);

        // Then
        assertEquals(dto.username(), username);
        assertEquals(dto.password(), password);
        assertEquals(dto.firstName(), firstName);
        assertEquals(dto.lastName(), lastName);
        assertEquals(dto.email(), email);
        assertEquals(dto.phoneNumber(), phoneNumber);
        assertEquals(dto.birthdate(), birthdate);
    }
}