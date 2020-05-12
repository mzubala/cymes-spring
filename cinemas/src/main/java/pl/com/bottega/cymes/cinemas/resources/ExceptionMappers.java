package pl.com.bottega.cymes.cinemas.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.GenericDao;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

public class ExceptionMappers {

    @Provider
    public static class EntityNotFoundExceptionMapper implements ExceptionMapper<GenericDao.EntityNotFoundException> {
        @Override
        public Response toResponse(GenericDao.EntityNotFoundException exception) {
            return Response.status(404).entity(new Error(exception)).build();
        }
    }

    @Provider
    public static class JPAEntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
        @Override
        public Response toResponse(EntityNotFoundException exception) {
            return Response.status(404).entity(new Error(exception)).build();
        }
    }

    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            return Response.status(400).entity(new ValidationErrors(exception.getConstraintViolations())).build();
        }
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