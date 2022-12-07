package pl.com.bottega.ecom.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.h2.api.ErrorCode;
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
        if (ex.getErrorCode() == ErrorCode.DUPLICATE_KEY_1) {
            return new ResponseEntity(new GlobalError("Uniqueness constraint violated on the db"), HttpStatus.CONFLICT);
        } else if(ex.getErrorCode() == ErrorCode.REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
            return new ResponseEntity(new GlobalError("Object to reference not found"), HttpStatus.NOT_FOUND);
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
