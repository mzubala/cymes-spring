package pl.com.bottega.cymes.cinemas.services.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.cinemas.dataaccess.dao.PersistentCommandDao;
import pl.com.bottega.cymes.cinemas.dataaccess.model.PersistentCommand;
import pl.com.bottega.cymes.cinemas.services.commands.UserCommand;

@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final PersistentCommandDao persistentCommandDao;

    private final ObjectMapper objectMapper;

    @After(value = "(@within(Audit) || @annotation(Audit)) && args(command)")
    public void saveCommand(UserCommand command) {
        persistentCommandDao.save(toPersistenetCommand(command));
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

