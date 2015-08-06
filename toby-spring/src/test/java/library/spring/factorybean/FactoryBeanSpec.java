package library.spring.factorybean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FactoryBeanSpec {

	@Autowired
	ApplicationContext context;

	@Test
	public void getMessageFromFactoryBean() {
		Object msg = context.getBean("message");
		assertThat(msg, instanceOf(Message.class));
		assertThat(((Message)msg).getMessage(), is("Example"));
	}
}

