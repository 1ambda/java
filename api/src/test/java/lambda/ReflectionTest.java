package lambda;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;


public class ReflectionTest {
  
  
  String name = "1ambda";
  MailContext mailContext = new MailContext(
      "lambda@service.com",
      "client@service.com",
      "Notification",
      "Your premium service expired"
      );
    

  @Test
  public void testLengthMethod() throws Exception {
   
    assertThat(name.length(), is(6));
    Method lengthMethod = String.class.getMethod("length");
    assertThat((Integer)lengthMethod.invoke(name), is(6));
  }
  
  @Test
  public void testCharAtMethod() throws Exception {
    assertThat(name.charAt(0), is('1'));
    Method charAtMethod = String.class.getMethod("charAt", int.class);
    assertThat((Character)charAtMethod.invoke(name, 0), is('1'));
  }
  
  @Test
  public void testMailSender() {
    // create proxy class without reflection API
    LogMailSender logMailSender = new LogMailSender();
    logMailSender.setMailSender(new SmtpMailSender());
    
    assertThat(logMailSender.send(mailContext), is(true));
  }
  
  @Test
  public void testDynamicProxyMailSender() {
    MailSender logProxy = (MailSender)Proxy.newProxyInstance(
            getClass().getClassLoader(), 
            new Class[] { MailSender.class }, 
            new LoggingHandler(new SmtpMailSender()));
    
    assertThat(logProxy.send(mailContext), is(true));
  }
}
