package com.example.botjournal.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
@Aspect
@Slf4j
public class LogConfiguration {

    @Pointcut("execution(public * com.example.botjournal.sevices.*.*(..))")
    public void serviceLog() {}

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint jp) {
        log.info("""
                RUN SERVICE
                SERVICE_METHOD{}.{}
                """, jp.getSignature().getDeclaringTypeName(), jp.getSignature().getName());
    }

    @AfterThrowing(throwing = "e", pointcut = "serviceLog()")
    public void doAfterThrowing(JoinPoint jp, Exception e) {
        log.error("""
                Request throw an Exception. Cause - {}.{}
                """, Arrays.toString(jp.getArgs()), e.getMessage());
    }

}
