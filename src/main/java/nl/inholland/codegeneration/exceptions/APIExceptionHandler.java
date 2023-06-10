package nl.inholland.codegeneration.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import nl.inholland.codegeneration.models.DTO.response.APIExceptionResponseDTO;
import org.hibernate.query.SemanticException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    public ResponseEntity<APIExceptionResponseDTO> handleNotFoundException(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Not Found!",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleForbiddenException(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Forbidden!",
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({BadCredentialsException.class, JwtException.class, UsernameNotFoundException.class, AccountExpiredException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleUnauthorizedException(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? ex.getMessage() : "Unauthorized!",
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, SemanticException.class, NullPointerException.class, IllegalArgumentException.class, NoSuchFieldException.class, IllegalStateException.class, HttpMediaTypeException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleBadRequestException(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                (ex.getMessage() != null) ? (ex instanceof NoSuchFieldException ? "No such field: " : "") + ex.getMessage() : "Bad Request!",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<APIExceptionResponseDTO> handleBadRequestExceptionByConstraint(Exception ex, WebRequest request) {
        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
                ex.getClass() == MethodArgumentNotValidException.class ? ((MethodArgumentNotValidException)ex).getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString() :
                    (ex.getClass() == ConstraintViolationException.class ? ((ConstraintViolationException)ex).getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList().toString() : "Bad Request!"),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
    }

//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<APIExceptionResponseDTO> handleUnprocessableEntityException(Exception ex, WebRequest request) {
//        APIExceptionResponseDTO apiExceptionResponseDTO = new APIExceptionResponseDTO(
//                (ex.getMessage() != null) ? ex.getMessage() : "Unprocessable Entity!",
//                HttpStatus.UNPROCESSABLE_ENTITY,
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(apiExceptionResponseDTO, apiExceptionResponseDTO.httpStatus());
//    }

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
