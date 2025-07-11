# 토비의 클린 스프링 - 도메인 모델 패턴과 헥사고날 아키텍처 Part 1

# 메모
소프트웨어 개발에서의 도메인
- 사용자가 프로그램 또는 소프트웨어 서비스를 적용하는 주제 영역을 도메인이라고 한다. 
- 소프트웨어가 도메인을 반영하도록 만들어야 한다. 

도메인 모델
- 소트웨어는 도메인의 핵심 개념과 요소들을 통합하고, 그 관계를 정확하게 구현해야 한다. 
- 소프트웨어는 도메인을 모델링해야한다. 단순히 코드로 직접 옮길 수 없다. 따라서, 도메인의 추상화인 도메인 모델을 만들어야한다. 
- 도메인 모델은 소프트웨어가 해결하려는 특정 문제 영역(도메인)의 핵심지도
- 도메인에 존재하는 중요한 개념과 이들 사이의 관계, 그리고 규칙을 표현

도메인 주도 설계
- 도메인의 복잡성이 주는 문제를 해결하기 위한 접근 방법
- 도메인 모델을 개발 과정의 중심에 두는 방법
- __개발자 뿐 아니라 도메인을 잘 아는 현업 전문가, 이해 관계자가 모두 참여해서 함께 도메인모델을 만들고 발전시켜야한다.__
- 모델 주도 설계: 도메인 모델이 설계와 코드까지 이어져야 한다
- 유비쿼터스언어: 팀 안에서 도메인 모델에 기반한 단일 어휘체계를 만들고, 이를 문서, 회의, 대화, 그리고 코드까지 일관되게 사용한다. 

도메인 모델 만들기
1. 듣고 배우기
2. 중요한 것들 찾기 (개념 식별)
3. 연결고리 찾기 (관계 정의)
4. '것'들을 설명하기
5. 그려보기 (시각화)
6. 이야기하고 다듬기 (반복)

엔티티
- 도메인 모델을 만들 때 사용하는 패턴
- 도메인 안에 있는 대상이나 개념
- 고유한 식별자(identity)를 가지고 이를 통해서 개별적으로 구분된다.
- 생명주기를 가진다. 시간의 흐름에 따라 상태가 변경될 수 있다. 

도메인 모델 패턴
- 도메인/비지니스 로직을 구성하는 아키텍처 패턴의 한 가지
- 도메인 모델의 속성과 행위를 모두 포함하는 도메인의 오브젝트 모델이다. 
- 오브젝트 모델이기 때문에 복잡한 연관 관계, 커스텀 속성, 상속 등을 사용할 수 있다. 

값 객체
- 도메인 모델에서 식별자가 필요하지 않고 속성/값으로만 구별되는 오브젝트
- 엔티티가 너무 많은 책임을 가지는것을 방지하고 특정 속성 관련 행위를 분리해서 엔티티를 더 집중된 상태로 유지하게 한다. 
- 원시 타입보다는 도메인 개념을 더 명시적으로 나타내서 모델의 명확성을 높인다. 
- 생성 이후에 상태가 변하지 않고 변경이 필요하면 새로운 객체로 교체한다. 
- 풍부한 기능을 가진다
- 자체 유효성 검사도 가능하다

도메인 모델 패턴 스타일
- 단순 도메인 모델 
    - DB 설계와 유사. 테이블 하나에 대해 하나의 도메인 오브젝트
- 풍성한 도메인 모델 
    - 상속, 전략, GoF 디자인패턴, 연관관계
    - 복잡한 로직에 적합하지만 DB 매핑이 어려울 수 있다

도메인 로직의 API 개발
- 도메인 모델 패턴은 트랜잭션 스크립트 처럼 작업 단위의 절차형 API를 만들기가 어렵다. 
- 도메인 로직의 명확한 작업 단위 API를 제공하는 애플리케이션 서비스가 필요

헥사고날 아키텍처
- 2005년 앨리스터 코번이 제안한 아키텍처
- 계층형 아키텍처의 단방형 비대칭 구조가 아닌 대칭형(symmetric) 아키텍처
- 위 아래, 좌 우가 아닌 애플리케이션의 내부와 외부 세계라는 대칭구조를 가진다
- 대칭성을 가진 구조를, 그리기 쉬운 대표적인 도형이 육각형(hexagonal)으로 설명

헥사곤 내부
- 쉽게 변하지 않는, 중요한 도메인 로직을 담은 코어 애플리케이션

헥사곤 외부
- 헥사곤과 상호작용하는 모든 것 - 액터
- 사용자, 브라우저, CLI 명령, 기계, 다른 시스템
- 운영 환경, DB, 메시징 시스템, 메일 시스템, 원격 서비스
- 테스트

헥사고날 아키텍처의 특징과 혜택
- 테스트. 운영 시스템에 연결되지 않고 애플리케이션 테스트
- 액터가 바뀌더라도 다시 빌드하지 않는 테스트
- UI 디테일이나 기술 정보가 도메인 로직 안으로 노출되지 않도록 보호한다. 
- 컴포넌트를 각각 개발하고 연결하는 방식으로 큰 시스템을 분리할 수 있다. 
- 기술 요소를 제거했기 때문에 도메인 설계에 집중할 수 있다. 

포트
- 애플리케이션이 외부 세계와 의도(intention)를 가지고 상호작용 하는 아이디어를 캡처한 것
- 단순히 데이터를 주고 받는게 아니라, 명확한 목적과 방향을 가지고 외부와 연결된다
- 애플리케이션의 정의한 인터페이스

어댑터
- 애플리케이션의 포트를 액터가 직접 연결할 수 없다면 인터페이스 변환을 위한 어댑터를 도입
- 예:
   ```
   <Actor> - <Adapter> - <Port> - <Application(Hexagon)>
   ```

헥사고날 아키텍처의 비대칭성
- 애플리케이션이 제공하는 기능을 사용하는 액터와 이를 위한 어댑터
    - primary actor, primary adapter
    - driving actor, driving adapter
- 애플리케이션이 동작하는데 필요한 기능을 제공하는 액터와 이를 위한 어댑터
    - secondary actor, secondary adapter
    - driven actor, driven adapter

오해
- 애플리케이션 내부에 도메인 계층으로 만들어야 한다.
    - 헥사고날 아키텍처는 애플리케이션 내부 구현에 대한 원칙이나 요구사항이 없다. 
    - 도메인 계층을 포함하는 아키텍처는 클린 아키텍처이다. 헥사고날 아키텍처는 클린아키텍처는 아니다. 
- 헥사고날 아키텍처 패키지 구조를 따라야 한다
    - 헥사고날 아키텍처가 요구하는 피키지 구조는 없다
    - 애플리케이션과 어댑터 패키지를 분리하는 것은 바람직하다
    - 포트를 구분된 패키지에 두는 것을 권장한다
- UseCase라는 접미사를 사용한다
    - 포트의 의도를 담은 이름을 사용하면 된다
    - `For + ~ing` 스타일의 권장 네이밍이 있지만 이를 따를 필요는 없다.
- 애플리케이션에는 도메인 모델만 넣고 JPA 엔티티등은 어댑터에 둬야한다
    - 애플리케이션 코드와 포트 인터페이스가 외부 기술에 의존하지 않으면 된다

헥사고날 아키텍처가 요구하는것 
- 애플리케이션은 모든 외부와의 상호작용을 위해서 `provided interface`와 `required interface`를 정의한다. 
- 애플리케이션과 상호작용하는 액터는 런타임에 구성되어야한다. 
- 애플리케이션은 액터에 대한 코드 의존성을 가지면 안된다. 
- 액터는 정의된 포트를 통해서만 연결해야 한다. 
- 포트의 인터페이스에는 기술 의존성을 가지지 않는다.

헥사고날과 도메인 모델 패턴을 적용한 대칭형 계층 구조
- 외부에서 내부로 향하는 일종의 계층 구조
- 코드의 의존 방향은 내부로만 향한다
    - Adapter -> Application -> Domain
- 단, 사용의 흐름은 비대칭적이다

엔티티 식별자
- 고유성: 두개의 엔티티가 같은 값을 가지면 안된다
- 불변성: 한 번 값이 할당되면 엔티티의 생명주기 동안 절대 변경되면 안된다
- 비지니스 의미로부터 디커플링 되는 것이 낫다

Hibernate - @NaturalId, @NaturalIdCache
- hibernate에서 자연키로 사용할 필드를 지정한다. 
- @NaturalIdCache 를 추가하면 조회 시 2차 캐시를 사용해서 성능을 개선할 수 있다. 

테스트
- interface를 테스트 해야한다.
    - 예를 들어서 MemberService가 MemberRegister를 구현한다면, MemberRegister 인터페이스를 테스트 해야 한다. 
- java17 이상부터는 record로 테스트를 할 수 있다. 
- 테스트용 빈이 필요할 경우 `@TestConfiguration` 애노테이션을 이용해서 테스트 설정 클래스를 넣어줄 수 있다. 
   ```java
    @TestConfiguration
    public class SplearnTestConfiguration {

        @Bean
        public EmailSender emailSender() {
            return (email, subject, body) -> System.out.println("sending email: " + email);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return MemberFixture.createPasswordEncoder();
        }
    }
   ```
- console out에 대한 테스트
   ```java
   // gradle
   testImplementation("org.junit-pioneer:junit-pioneer:2.3.0")

   // 사용 코드
   @Test
   @StdIo   // 1. 애노테이션 정의
   void dummyEmailSender(StdOut out) {  // 2. out은 테스트 중에 발생한 출력 정보를 가지고 있다. 
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("test@test.com"), "subject", "body");

        assertThat(out.capturedLines()[0])
        .isEqualTo("DummyEmailSender: send mail: Email[address=test@test.com]");
   }
   ```