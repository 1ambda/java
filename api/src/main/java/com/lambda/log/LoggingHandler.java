package com.lambda.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class LoggingHandler implements InvocationHandler {

  MailSender target;
  
  private Logger logger;
  
  public LoggingHandler(MailSender target) {
    this.target = target;
    
    logger = Logger.getLogger("LoggingHandler");
    
    Appender appender;  
    logger.setLevel(Level.ALL);  
    appender = new ConsoleAppender(new SimpleLayout());  
    logger.addAppender(appender); 
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    
    Object result = method.invoke(target, args);
    
    if (method.getName().equals("send")) {
      logger.info("mail sent and logging using dynamic proxy");
    }

    return result;
  }

}
