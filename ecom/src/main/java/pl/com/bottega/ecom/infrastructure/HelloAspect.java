package pl.com.bottega.ecom.infrastructure;

import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pl.com.bottega.ecom.UserCommand;

@Log
@Component
@Aspect
class HelloAspect {
    @Before("execution(* pl.com.bottega.ecom.catalog.*.*(..))")
    void sayHello(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if(args.length == 1 && args[0] instanceof UserCommand) {
            var userCommand = (UserCommand) args[0];
            userCommand.getUserId();
        }
        log.info(String.format("Hello!!! %s", joinPoint.getSignature().toString()));
    }

    @Around("@within(pl.com.bottega.ecom.infrastructure.Timed) || @annotation(pl.com.bottega.ecom.infrastructure.Timed)")
    Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        var ts = System.currentTimeMillis();
        var value = joinPoint.proceed();
        var te = System.currentTimeMillis();
        log.info(String.format("Execution of %s took %d", joinPoint.getSignature().toString(), te - ts));
        return value;
    }
}
