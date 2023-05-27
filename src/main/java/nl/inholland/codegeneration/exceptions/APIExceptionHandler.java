package nl.inholland.codegeneration.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import nl.inholland.codegeneration.models.DTO.response.APIExceptionResponseDTO;
import org.hibernate.query.SemanticException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIExceptionResponseDTO> handleAPIException(APIException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Internal Server Error!",
                (ex.getHttpStatus() != null) ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR,
                (ex.getHttpStatus() != null) ? ex.getTimestamp() : LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIExceptionResponseDTO> handleNotFoundException(EntityNotFoundException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Not Found!",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleForbiddenException(RuntimeException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Forbidden!",
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({BadCredentialsException.class, JwtException.class, UsernameNotFoundException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleUnauthorizedException(RuntimeException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Unauthorized!",
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, SemanticException.class, NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleBadRequestException(RuntimeException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Bad Request!",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIExceptionResponseDTO> handleUnprocessableEntityException(IllegalStateException ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Unprocessable Entity!",
                HttpStatus.UNPROCESSABLE_ENTITY,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIExceptionResponseDTO> handleException(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Internal Server Error!",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }
}
