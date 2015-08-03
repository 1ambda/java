# 1. 서론

# 현대 시대는 **객체** 를 **관계형 데이터베이스** 에 저장하는 시대,

문제가 생겼을 때 SQL 을 까봐야함 그 전에는 Entity 를 믿을 수 없음.
그런데 계층형 아키텍쳐에서는 진정한 의미의 계층 분할이 어렵다.

SQL 에 의존적인 개발을 하기가 힘들다.

# 패러다임의 불일치 (객체 vs 관계)

객체와 관계형 데이터베이스의 차이

- 상속
- 연관관계
- 데이터 타입
- 데이터 식별 방법

객체는 자유로운 객체 그래프 탐색할 수 있어야함. `member.getOrder().getOrderItem().getCategories()`

그러나 SQL 에서 막힘. 어디까지 SQL 을 짜야하나...

쿼리에 따라서 객체 그래프의 탐색범위가 정해짐. 어디서 NPE 가 날지 알 수 없음.
SQL 을 까봐야함.


```java
m1 = memberDAO.getMember(memberId)
m2 = memberDAO.getMember(memberId)

m1 == m2 // 다름. 근데 오 다르지? dao 에서 new Member 할테니까


// 그러나 자바 컬렉션에서는 같음
```

**객체답게 모델링 할수록 매핑작업만 늘어난다.**

# 2. JPA

## ORM

객체는 객체대로 설계, 관계형 DB 는 관계대로 설계. 
ORM 프레임워크가 이 중간에서 매핑.

EJB -> Hibernate -> JPA

JPA 는 하이버네이트를 기반으로 나왔기 때문에 믿을 수 있음.

JPA 2.1 구현체

- Hibernate, EclipseLink, DataNucleus

## JPA 기초와 매핑 

# 객체 연관관계와 DB 연관관계는 다름

객체 연관관계는 단방향 2개를 양방향
DB 는 FK 하나로 양쪽으로 탐색할 수 있음.

# 영속성 컨텍스트

- 영속성 컨텍스트에 있을 경우는 `==` 을 지원






























