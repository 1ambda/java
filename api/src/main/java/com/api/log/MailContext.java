package com.api.log;

public class MailContext {
  
  private String to;
  private String from;
  private String title;
  private String subject;

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  public MailContext() {
    
  }
  
  public MailContext(
      String to,
      String from,
      String title,
      String subject) {
   
    this.to = to;
    this.from = from;
    this.title = title;
    this.subject = subject;
  }
  
}
