package nl.inholland.codegeneration.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


// API Error record used as the response in the APIExceptionHandler
public record APIExceptionResponse(
    String message,
    HttpStatus httpStatus,
    LocalDateTime timestamp
) {

}
