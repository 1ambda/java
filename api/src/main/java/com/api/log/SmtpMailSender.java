package com.api.log;

public class SmtpMailSender implements MailSender {
  
  private void sendSmtpMail(MailContext context) {
    // do nothing
  }

  @Override
  public boolean send(MailContext context) {
   
    sendSmtpMail(context);
    return true;
  }

}