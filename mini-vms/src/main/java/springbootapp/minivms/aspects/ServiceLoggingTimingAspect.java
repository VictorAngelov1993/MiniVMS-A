package springbootapp.minivms.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ServiceLoggingTimingAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingTimingAspect.class);

    @Pointcut("execution(* springbootapp.minivms.services..*(..))")
    public void serviceLayerMethods() {

    }
    @Around("serviceLayerMethods()")
    public Object logAndTimeServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodSignature = joinPoint.getSignature().toShortString();
        String sanitizedArgs = formatArgs(joinPoint.getArgs());
        long startNanos = System.nanoTime();

        log.info("--> Entering {} with args: {}", methodSignature, sanitizedArgs);

        try{
            Object result = joinPoint.proceed();
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            log.info("<-- Completed {} in {} ms", methodSignature, elapsedMs);
            return result;
        } catch (Throwable ex) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            log.warn("<!- Failed {} after {} ms with {}: {}",
                    methodSignature, elapsedMs, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }

    }



    private String formatArgs(Object[] args) {
        if(args == null || args.length == 0) {
            return "[]";
        }
        return Arrays.stream(args)
                .map(arg -> arg == null ? "null" : arg.getClass().getSimpleName())
                .toList()
                .toString();
    }
}
