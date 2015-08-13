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


### DefaultTransactionDefinition

`TransactionDefinition` 인터페이스는 트랜잭션 동작 방식에 영향을 줄 수 있는 네가지 속성을 정의하고 있다.

#### Transaction Propagation

이미 진행중인 트랜잭션이 있을 경우, 없을 경우에 어떻게 동작하는지를 결정한다.

- **PROPAGATION_REQUIRED** 진행 중인 트랜잭션이 없을 경우 새로 시작하고, 있으면 이에 참여한다.
- **PROPAGATION_REQUIRES_NEW** 항상 새로운 트랜잭션을 시작한다.
- **PROPAGATION_NOT_SUPPORTED** 트랜잭션 없이 동작한다. 

트랜잭션 매니저를 통해 트랜잭션을 시작하려고 할 때 `getTransaction()` 을 사용하는 이유는, 바로 이 트랜잭션 전파 속성 때문이다. 항상 트랜잭션을 새로 시작하는 것이 아니라, 
트랜잭션이 이미 있는지 확인하고 현재 속성에 따라서 행동한다.

#### Isolation Level

`DefaultTransactionDefinition` 은 `DataSource` 에 지정된 격리 수준을 따른다.

#### Timeout

`DefaultTransactionDefinition` 은 기본 설정은 제한시간이 없는 것이다.

#### Read Only

<br/>

위 4가지 속성을 설정하기 위해 스프링이 제공하는 `TransactionInterceptor` 를 이용할 수 있다. `TransactionInterceptor` 는 
네 `PlatformTransactionManager` 와 `TransactionAttribute` 를 멤버로 가지는데, `TransactionAttribute` 의 경우 
위 4가지 속성을 설정할 수 있고 `rollbackOn` 이용해 예외 발생시 행동을 지정할 수 있다.

일반적으로 스프링은 런타임예외시 트랜잭션을 롤백하고, 체크예외시 의미있는 비즈니스 로직이라 보고 트랜잭션을 커밋한다. `rollbackOn` 메소드를 이용하면 이것을 변경할 수 있다.

<br/>

### 프록시 방식 AOP 는 같은 타깃 오브젝트 내의 메소드를 호출할 때는 적용되지 않는다.

### @Transactional

`@Transactional` 을 사용할 경우, `TransactionInterceptor` 는 메소드 이름 패턴 대신 `@Transactional` 에서 트랜잭션 속성을 가져오는 `AnnotationTransactionAttributeSource` 를 사용한다. 

참고로, 인터페이스 프록시 AOP 가 아닌 다른 방식의 프록시를 사용할 경우 인터페이스에 작성된 `@Transactional` 는 동작하지 않으므로, 클래스에 어노테이션을 추가하는 편이 낫다.
  
```java
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan("com.github.lambda")
public class TestAppConfig {
    ...
}


```



