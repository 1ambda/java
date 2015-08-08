package com.github.lambda.service;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
public class TransactionAspect {
    @Autowired
    PlatformTransactionManager manager;

    @Around("execution (* *..*.upgradeLevels(..))")
    public Object transactAround(ProceedingJoinPoint joinPoint) throws Throwable {

        TransactionStatus status = manager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object result = joinPoint.proceed();
            manager.commit(status);
            return result;
        } catch (RuntimeException e) {
            manager.rollback(status);
            throw e;
        }
    }
}
