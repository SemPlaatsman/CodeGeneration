package nl.inholland.codegeneration.models.DTO.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


// API Error record used as the response in the APIExceptionHandler
public record APIExceptionResponseDTO (
    String message,
    HttpStatus httpStatus,
    LocalDateTime timestamp
) {

}
