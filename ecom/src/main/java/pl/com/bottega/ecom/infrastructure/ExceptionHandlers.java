package pl.com.bottega.ecom.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
class ExceptionHandlers {

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

    @Data
    @AllArgsConstructor
    static class GlobalError {
        String errorMessage;
    }

}
