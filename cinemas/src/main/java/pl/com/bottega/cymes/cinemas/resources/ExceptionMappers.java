package pl.com.bottega.cymes.cinemas.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.com.bottega.cymes.cinemas.dataaccess.PSQLExceptionWrapper;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.GenericDao;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class ExceptionMappers extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Stream<ValidationError> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map((error) ->
                new ValidationError(error.getField(), error.getDefaultMessage())
        );
        Stream<ValidationError> globalErrors = ex.getBindingResult().getGlobalErrors().stream().map((error) ->
                new ValidationError(error.getObjectName(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(new ValidationErrors(Stream.concat(fieldErrors, globalErrors).collect(toSet())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericDao.EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFoundException(GenericDao.EntityNotFoundException ex) {
        return new ResponseEntity<>(new Error(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleJPAEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new Error(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Error> handlePSQLException(PSQLException ex) throws Exception {
        PSQLExceptionWrapper psqlExceptionWrapper = new PSQLExceptionWrapper(ex);
        if (psqlExceptionWrapper.isUniqueConstraintViolation()) {
            return new ResponseEntity<>(new Error(ex.getMessage()), CONFLICT);
        }
        throw ex;
    }

    @Data
    @AllArgsConstructor
    public static class Error {
        private String errorMessage;

        Error(Exception ex) {
            this(ex.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    public static class ValidationErrors {
        private Set<ValidationError> errors;
    }

    @Data
    @AllArgsConstructor
    public static class ValidationError {
        private String message;
        private String field;
    }
}