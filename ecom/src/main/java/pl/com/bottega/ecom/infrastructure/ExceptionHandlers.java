package pl.com.bottega.ecom.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<GlobalError> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity(new GlobalError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    ResponseEntity<GlobalError> handleJdbcSQLIntegrityConstraintViolationException(JdbcSQLIntegrityConstraintViolationException ex) throws JdbcSQLIntegrityConstraintViolationException {
        if (ex.getErrorCode() == 23505) {
            return new ResponseEntity(new GlobalError("Uniquness constraint vioated on the db"), HttpStatus.CONFLICT);
        }
        throw ex;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ValidationErrors(
            ex.getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList())
        ), HttpStatus.BAD_REQUEST);

    }

    @Data
    @AllArgsConstructor
    static class GlobalError {
        String errorMessage;
    }

    @Data
    @AllArgsConstructor
    static class ValidationErrors {
        List<ValidationError> errors;
    }

    @Data
    @AllArgsConstructor
    static class ValidationError {
        String fieldName;
        String message;
    }

}
