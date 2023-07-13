package com.teamsapi.custom_annotation.aspect;

import com.teamsapi.custom_annotation.annotation.MethodExecutionTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

@Aspect
@Component
public class MethodExecutionTimeAspect {
    private static final Logger logger = LoggerFactory.getLogger(MethodExecutionTimeAspect.class);
    @Around("@annotation(com.teamsapi.custom_annotation.annotation.MethodExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result= joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MethodExecutionTime methodExecutionTime = method.getAnnotation(MethodExecutionTime.class);
        int threshold =methodExecutionTime.threshold();
        String joinPointSignature = joinPoint.getSignature().toShortString();
        if (threshold < executionTime) {
            logger.warn("\u001B[31mMethod {} took {} milliseconds to execute, threshold was {} milliseconds.\u001B[0m",joinPointSignature , executionTime, threshold);
        } else {
            logger.info("Method {} took {} milliseconds to execute, threshold was {} milliseconds.", joinPointSignature, executionTime,  threshold);
        }
        return result;
    }
}