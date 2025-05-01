package study.phonemanagement.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Around("execution(* study.phonemanagement..*Repository*.*(..))")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.debug("[start - Repository] - {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.debug("[end - Repository] - {}, 실행 시간 : {}ms", joinPoint.getSignature(), endTime - startTime);

        return result;
    }

    @Around("execution(* study.phonemanagement..*Service*.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.debug("[start - Service] - {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.debug("[end - Service] - {}, 실행 시간 : {}ms", joinPoint.getSignature(), endTime - startTime);

        return result;
    }

    @Around("execution(* study.phonemanagement..*Controller*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.debug("[start - Controller] - {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.debug("[end - Controller] - {}, 실행 시간 : {}ms", joinPoint.getSignature(), endTime - startTime);

        return result;
    }
}
