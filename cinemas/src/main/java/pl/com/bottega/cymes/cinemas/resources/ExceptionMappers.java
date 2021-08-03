package pl.com.bottega.cymes.cinemas.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.GenericDao;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@ControllerAdvice
public class ExceptionMappers {

    @ExceptionHandler(GenericDao.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Error> handleEntityNotFoundException(GenericDao.EntityNotFoundException ex) {
        return new ResponseEntity<>(new Error(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Error> handleJPAEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new Error(ex), HttpStatus.NOT_FOUND);
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
    public static class ValidationErrors {

        private Set<ValidationError> errors;

        public ValidationErrors(Set<ConstraintViolation<?>> violations) {
            Map<String, Set<String>> errorsMap = violations.stream().collect(
                groupingBy(
                    (violation) -> violation.getPropertyPath().toString(), mapping(ConstraintViolation::getMessage, toSet())
                )
            );
            this.errors = errorsMap.entrySet().stream()
                .map((entry) -> new ValidationError(entry.getKey(), entry.getValue()))
                .collect(toSet());
        }

    }

    @Data
    public static class ValidationError {
        private Set<String> messages;
        private String field;

        ValidationError(String field, Set<String> messages) {
            this.messages = messages;
            this.field = field;
        }
    }
}