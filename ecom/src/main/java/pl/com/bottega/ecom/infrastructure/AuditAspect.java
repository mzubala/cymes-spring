package pl.com.bottega.ecom.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.com.bottega.ecom.UserCommand;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
@RequiredArgsConstructor
class AuditAspect {

    private final PersistentCommandRepository repository;
    private final ObjectMapper objectMapper;

    @After("@within(pl.com.bottega.ecom.infrastructure.Audit) || @annotation(pl.com.bottega.ecom.infrastructure.Audit)")
    void audit(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
            .filter(arg -> arg instanceof UserCommand)
            .map(arg -> (UserCommand) arg)
            .map(this::toPersistentCommand)
            .forEach(repository::save);
    }

    @SneakyThrows
    private PersistentCommand toPersistentCommand(UserCommand userCommand) {
        return new PersistentCommand(
            UUID.randomUUID(),
            userCommand.getUserId(),
            Instant.now(),
            objectMapper.writeValueAsString(userCommand)
        );
    }
}
