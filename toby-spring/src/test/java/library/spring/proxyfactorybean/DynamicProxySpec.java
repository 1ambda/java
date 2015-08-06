package library.spring.proxyfactorybean;

import library.spring.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DynamicProxySpec {

    @Test
    public void test_JDK_Proxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloImpl())
        );

        checkProxiedHello(proxiedHello);
    }

    @Test
    public void test_Spring_ProxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloImpl());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        checkProxiedHello(proxiedHello);
    }

    private void checkProxiedHello(Hello proxiedHello) {
        assertThat(proxiedHello.sayHi("lambda")).isEqualTo("HI LAMBDA");
        assertThat(proxiedHello.sayHello("lambda")).isEqualTo("HELLO LAMBDA");
        assertThat(proxiedHello.sayThankYou("lambda")).isEqualTo("THANK YOU LAMBDA");
    }
}

