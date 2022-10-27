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

    @After("(@within(pl.com.bottega.ecom.infrastructure.Audit) || @annotation(pl.com.bottega.ecom.infrastructure.Audit)) " +
        "&& args(command)")
    void audit(UserCommand command) {
        repository.save(toPersistentCommand(command));
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
