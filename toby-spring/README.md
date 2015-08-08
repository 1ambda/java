# Spring

## Chapter 6

**데코레이터 패턴을 적용한 프록시** 는 두 가지 문제점이 있다.

- 프록시 대상의 인터페이스를 매번 구현해야 한다.
- 부가적인 기능이 여러 메소드에 반복적으로 나타나서 코드 중복의 문제가 발생한다

**프록시 팩토리 빈** 은 다음과 같은 문제점이 있다.

- 한 클래스 안의 여러 메소드에 부가기능을 적용하는 것은 쉽지만, 여러 클래스에는 어렵다
- 하나의 타깃에 여러 개의 부가기능을 적용하는것도 번거롭다
- 같은 부가기능임애도 불구하고 타깃 오브젝트가 달라지만 Handler 클래스를 매번 새로 생성해야 한다. 

```java
public class TxFactoryBean implements FactoryBean<Object> {
	
	private Object target;
	private String pattern;
	private PlatformTransactionManager txManager;
	private Class<?> serviceInterface;

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public Object getObject() throws Exception {
		
		TxHandler txHandler = new TxHandler();
		txHandler.setPattern(pattern);
		txHandler.setTarget(target);
		txHandler.setTxManager(txManager);
		
		return Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { serviceInterface },
				txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
```

### ProxyFactoryBean

스프링의 `ProxyFactoryBean` 은 프록시 생성작업만을 담당하고, 부가기능은 별도의 빈에 둘 수 있다. 프록시에서 사용할 부가기능은 
`MethodInterceptor` 인터페이스를 구현해서 만든다. 

- `InvocationHandler.invoke()` 는 타깃 오브젝트에 대한 정보를 제공하지 않으므로, 타깃을 `InvocationHandler` 가 구현한 클래스가 직접 알고 있어야 한다.
- `MethodInterceptor.invoke()` 는 `ProxyFactoryBean` 으로부터 타깃을 제공받으므로, 타깃 오브젝트와 상관 없이 독립적으로 만들어질 수 있다. 따라서 여러 프록시에서 함께 사용 가능하며, 싱글톤 빈으로도 등록이 가능하다.

```java
@Bean(name = "userService")
public ProxyFactoryBean getUserService() throws Exception {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(userServiceImpl);
    pfBean.setInterceptorNames("transactionAdvisor");

    return pfBean;
}

@Bean
public NameMatchMethodPointcut getTransactionPointcut() {
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("upgrade*");
    return pointcut;
}

@Bean
public TransactionAdvice getTransactionAdvice() {
    return new TransactionAdvice();
}

@Bean(name = "transactionAdvisor")
public DefaultPointcutAdvisor getTransactionAdvisor() {
    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
    advisor.setPointcut(transactionPointcut);
    advisor.setAdvice(transactionAdvice);

    return advisor;
}

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
```

포인트컷과 어드바이스를 재활용할 수 있으나, 그 때 마다 `ProxyFactoryBean` 를 새롭게 생성해야 한다. 

### DefaultAdvisorAutoProxyCreator

`DefaultAdvisorAutoProxyCreator` 는 등록된 빈 중에서 `Advisor` 를 구현한 것을 모두 찾는다. 그리고 생성된 모든 빈에 대해서 
`Pointcut` 을 적용해 보면서 대상을 찾아 `Advice` 를 제공하는 프록시를 생성한다.

### Annotation-based AOP

```java
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
```

```java
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan("com.github.lambda")
public class TestAppConfig {
    ...
    ...
```