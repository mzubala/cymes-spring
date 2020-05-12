package pl.com.bottega.cymes.cinemas.services.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.PersistentCommandDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.PersistentCommand;
import pl.com.bottega.cymes.cinemas.resources.security.UserProvider;
import pl.com.bottega.cymes.cinemas.services.commands.UserCommand;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Audit
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class AuditInterceptor {

    @Inject
    private UserProvider userProvider;

    @Inject
    private PersistentCommandDao persistentCommandDao;

    @Inject
    private ObjectMapper objectMapper;

    @AroundInvoke
    public Object saveCommand(InvocationContext ctx) throws Exception {
        var userId = userProvider.currentUserId();
        var commands = Stream.of(ctx.getParameters())
            .filter((param) -> param instanceof UserCommand)
            .map((param) -> (UserCommand) param)
            .map((cmd) -> cmd.withUserId(userId))
            .collect(Collectors.toSet());
        var result = ctx.proceed();
        commands.stream().map(this::toPersistenetCommand).forEach(persistentCommandDao::save);
        return result;
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

