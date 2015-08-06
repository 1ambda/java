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