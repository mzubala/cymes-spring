package pl.com.bottega.cymes.cinemas.services.interceptors;

import pl.com.bottega.cymes.cinemas.services.commands.UserCommand;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Interceptor
@ValidateCommand
@Priority(Interceptor.Priority.APPLICATION + 10)
public class ValidationInterceptor {

    @Inject
    private Validator validator;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        var errors = Stream.of(ctx.getParameters())
            .filter((param) -> param instanceof UserCommand)
            .map((param) -> (UserCommand) param)
            .reduce(new HashSet<>(), this::validate, this::combine);
        if(errors.isEmpty()) {
            return ctx.proceed();
        } else {
            throw new ConstraintViolationException(errors);
        }
    }

    private Set<ConstraintViolation<UserCommand>> combine(Set<ConstraintViolation<UserCommand>> set1, Set<ConstraintViolation<UserCommand>> set2) {
        set1.addAll(set2);
        return set1;
    }

    private Set<ConstraintViolation<UserCommand>> validate(Set<ConstraintViolation<UserCommand>> acc, UserCommand userCommand) {
        var errors = validator.validate(userCommand);
        acc.addAll(errors);
        return acc;
    }
}
