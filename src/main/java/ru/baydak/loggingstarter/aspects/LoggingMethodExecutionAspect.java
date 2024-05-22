package ru.baydak.loggingstarter.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;
import ru.baydak.loggingstarter.annotations.LoggableInfo;

/**
 * This aspect logs the execution of methods annotated with @LoggableInfo.
 * It logs the start and end of the method execution, as well as the execution time.
 */
@Aspect
@Slf4j
public class LoggingMethodExecutionAspect {

    /**
     * Pointcut that matches methods annotated with @LoggableInfo.
     */
    @Pointcut("(within(@ru.baydak.loggingstarter.annotations.LoggableInfo *) || execution(@ru.baydak.loggingstarter.annotations.LoggableInfo * *(..))) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    /**
     * Advice that wraps around the methods matched by the pointcut.
     * It logs the start and end of the method execution, as well as the execution time.
     *
     * @param pjp the proceeding join point
     * @return the result of the method execution
     * @throws Throwable if an error occurs during the method execution
     */
    @Around("annotatedByLoggable()")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        var methodSignature = (MethodSignature) pjp.getSignature();
        var methodName = methodSignature.getName();
        var className = methodSignature.getDeclaringType().getSimpleName();

        log.info("Executing method {} in class {}", methodName, className);

        LoggableInfo loggableInfo = methodSignature.getMethod().getAnnotation(LoggableInfo.class);
        if (loggableInfo == null) {
            loggableInfo = methodSignature.getMethod().getDeclaringClass().getAnnotation(LoggableInfo.class);
        }

        String name = (loggableInfo != null) ? loggableInfo.name() : "";

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();

        log.info("Method {} in class {} completed in {} ms | {}", methodName, className, stopWatch.getTotalTimeMillis(), name);

        return result;
    }
}