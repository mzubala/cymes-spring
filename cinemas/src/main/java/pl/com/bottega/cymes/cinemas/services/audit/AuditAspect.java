package pl.com.bottega.cymes.cinemas.services.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.PersistentCommandDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.PersistentCommand;
import pl.com.bottega.cymes.cinemas.resources.security.UserProvider;
import pl.com.bottega.cymes.cinemas.services.commands.UserCommand;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final UserProvider userProvider;

    private final PersistentCommandDao persistentCommandDao;

    private final ObjectMapper objectMapper;

    @After(value = "@within(pl.com.bottega.cymes.cinemas.services.audit.Audit) || @annotation(pl.com.bottega.cymes.cinemas.services.audit.Audit)")
    public void saveCommand(JoinPoint ctx) throws Throwable {
        var userId = userProvider.currentUserId();
        var commands = Stream.of(ctx.getArgs())
            .filter((param) -> param instanceof UserCommand)
            .map((param) -> (UserCommand) param)
            .map((cmd) -> cmd.withUserId(userId))
            .collect(Collectors.toSet());
        commands.stream().map(this::toPersistenetCommand).forEach(persistentCommandDao::save);
    }

    private PersistentCommand toPersistenetCommand(UserCommand userCommand) {
        var persistentCommand = new PersistentCommand();
        try {
            persistentCommand.setContent(objectMapper.writeValueAsString(userCommand));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        persistentCommand.setUserId(userCommand.getUserId());
        persistentCommand.setType(userCommand.getClass().getSimpleName());
        return persistentCommand;
    }
}

