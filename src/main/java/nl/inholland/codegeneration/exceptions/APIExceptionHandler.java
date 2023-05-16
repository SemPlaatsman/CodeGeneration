package nl.inholland.codegeneration.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.SemanticException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.directory.InvalidSearchFilterException;
import java.time.LocalDateTime;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIExceptionResponse> handleException(APIException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Not Found!",
                (ex.getHttpStatus() != null) ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR,
                (ex.getHttpStatus() != null) ? ex.getTimestamp() : LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIExceptionResponse> handleException(EntityNotFoundException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Not Found!",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<APIExceptionResponse> handleException(InsufficientAuthenticationException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Forbidden!",
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler({BadCredentialsException.class, JwtException.class})
    public ResponseEntity<APIExceptionResponse> handleException(RuntimeException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Unauthorized!",
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, SemanticException.class})
    public ResponseEntity<APIExceptionResponse> handleException(InvalidDataAccessApiUsageException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Bad Request!",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIExceptionResponse> handleException(IllegalStateException ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Unprocessable Entity!",
                HttpStatus.UNPROCESSABLE_ENTITY,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIExceptionResponse> handleException(Exception ex, WebRequest request) {
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(
                (ex.getMessage() != null) ? ex.getMessage() : "Internal Server Error!",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.httpStatus());
    }
}
