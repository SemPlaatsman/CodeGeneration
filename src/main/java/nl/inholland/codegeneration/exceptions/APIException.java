package nl.inholland.codegeneration.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class APIException extends Exception {
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

    public APIException(String errorMessage, HttpStatus httpStatus, LocalDateTime timestamp) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
