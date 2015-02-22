package com.lambda.log;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class LogMailSender implements MailSender {
  
  Logger logger;
  
  public LogMailSender() {
    logger = Logger.getLogger("LogMailSender");
    Appender appender;  
    logger.setLevel(Level.ALL);  
    appender = new ConsoleAppender(new SimpleLayout());  
    logger.addAppender(appender); 
  }
  
  private MailSender target;
  public void setMailSender(MailSender target) {
    this.target = target;
  }

  @Override
  public boolean send(MailContext mc) {
    logging();
    return target.send(mc);
  }
  
  private void logging() {
    logger.info("mail sent and logging using proxy class");
  }

}
