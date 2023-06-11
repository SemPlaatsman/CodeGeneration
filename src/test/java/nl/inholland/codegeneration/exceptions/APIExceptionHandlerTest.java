package nl.inholland.codegeneration.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;

import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.DTO.response.APIExceptionResponseDTO;
import nl.inholland.codegeneration.services.AccountService;
import nl.inholland.codegeneration.services.mappers.AccountDTOMapper;

@ExtendWith(MockitoExtension.class)
public class APIExceptionHandlerTest {
  @Mock
  private WebRequest webRequest;

  @Mock
  private APIException apiException;

  private APIExceptionHandler apiExceptionHandler;

  @BeforeEach
  public void setup() {

    apiExceptionHandler = new APIExceptionHandler();
  }

  @Test
  void testHandleAPIException() {
    apiException = new APIException("Test exception", HttpStatus.BAD_REQUEST, LocalDateTime.now());

    // Call the method under test
    ResponseEntity<APIExceptionResponseDTO> response = apiExceptionHandler.handleAPIException(apiException, webRequest);

    // Assert the response
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    APIExceptionResponseDTO responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals("Test exception", responseBody.message());
    assertEquals(HttpStatus.BAD_REQUEST, responseBody.httpStatus());
    assertNotNull(responseBody.timestamp());

  }

  @Test
  void testHandleNotFoundException() {
    fail("Not yet implemented");

  }

  @Test
  void testHandleForbiddenException() {
    fail("Not yet implemented");

  }

  @Test
  void testHandleUnauthorizedException() {
    fail("Not yet implemented");

  }

  @Test
  void testHandleBadRequestException() {

  }

  @Test
  void testHandleBadRequestExceptionByConstraint() {
    fail("Not yet implemented");

  }

  @Test
  void testHandleException() {
    fail("Not yet implemented");

  }

}
