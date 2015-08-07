package com.github.lambda.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
    @Autowired
    PlatformTransactionManager manager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        TransactionStatus status = manager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object result = invocation.proceed();
            manager.commit(status);
            return result;
        } catch (RuntimeException e) {
            manager.rollback(status);
            throw e;
        }
    }
}
