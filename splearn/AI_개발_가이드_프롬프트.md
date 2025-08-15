# 🤖 헥사고날 아키텍처 + DDD 기반 Spring Boot 프로젝트 생성 가이드

## 📋 프롬프트 템플릿

**"헥사고날 아키텍처와 DDD 패턴을 적용한 Spring Boot 프로젝트를 생성해주세요."**

### 🎯 기본 요구사항

#### 1. 아키텍처 패턴
- **헥사고날 아키텍처** (포트와 어댑터 패턴)
- **도메인 주도 설계** (DDD)
- **의존성 방향**: 외부 → Adapter → Application → Domain

#### 2. 패키지 구조
```
src/main/java/[패키지명]/
├── domain/
│   ├── [도메인명]/     # 엔티티, 값객체, 도메인서비스, 예외
│   └── shared/         # 공유 도메인 객체 (Email, Money 등)
├── application/
│   └── [도메인명]/
│       ├── provided/   # Use Case 인터페이스 (외부에 제공)
│       ├── required/   # Repository/Service 인터페이스 (외부 의존성)
│       └── [구현체]    # @Service 클래스들
└── adapter/
    ├── webapi/         # @RestController, DTO
    ├── persistence/    # Repository 구현체
    ├── security/       # 보안 관련 어댑터
    └── integration/    # 외부 시스템 연동
```

#### 3. 기술 스택
- **Java 21**, **Spring Boot 3.x**
- **Spring Data JPA**, **Spring Validation**
- **Lombok**, **H2/MySQL**
- **JUnit 5**, **Mockito**

### 🏗️ 구현 규칙

#### Domain Layer
- `AbstractEntity` 상속하여 ID 관리
- 정적 팩토리 메서드로 생성 (`create`, `register`)
- 비즈니스 로직을 도메인 메서드로 구현
- **JPA 어노테이션 사용 금지** (순수 Java 객체 유지)

#### Application Layer
- `provided/`: Use Case 인터페이스 정의
- `required/`: Repository/Service 인터페이스 정의
- 구현체: `@Service`, `@Transactional`, `@Validated` 적용

#### Adapter Layer
- `@RestController`: REST API
- `@Repository`: 데이터 접근
- DTO는 각 어댑터별 하위 패키지에 위치

### 🗃️ 데이터 영속성: XML 기반 JPA 매핑

#### 필수 파일: `src/main/resources/META-INF/orm.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<entity-mappings xmlns="http://www.hibernate.org/xsd/orm/mapping">
  <access>FIELD</access>
  
  <!-- 공통 엔티티 -->
  <mapped-superclass class="[package].AbstractEntity">
    <attributes>
      <id name="id">
        <generated-value strategy="IDENTITY" />
      </id>
    </attributes>
  </mapped-superclass>
  
  <!-- 각 엔티티별 매핑 정의 -->
  <entity class="[package].[Entity]">
    <table name="[table_name]">
      <unique-constraint name="UK_[CONSTRAINT_NAME]">
        <column-name>[column_name]</column-name>
      </unique-constraint>
    </table>
    <attributes>
      <basic name="[fieldName]">
        <column name="[column_name]" nullable="false" length="[size]" />
      </basic>
      <basic name="status">
        <enumerated>STRING</enumerated>
      </basic>
      <embedded name="[embeddedField]" />
      <one-to-one name="[relationField]" fetch="LAZY">
        <cascade><cascade-all/></cascade>
      </one-to-one>
    </attributes>
  </entity>
</entity-mappings>
```

### 🧪 테스트 구조
- **도메인**: 단위 테스트 (순수 POJO)
- **애플리케이션**: Mockito 테스트
- **API**: `@SpringBootTest`, `MockMvcTester` 통합 테스트

---

## 📚 필수 문서 생성 및 관리

### 1. 도메인모델.md 📋

**프로젝트 루트에 생성하고 지속적으로 업데이트**

```markdown
# [프로젝트명] 도메인 모델

## 도메인 모델 만들기
1. 듣고 배우기
2. 중요한 것들 찾기 (개념 식별)
3. 연결고리 찾기 (관계 정의)
4. '것'들을 설명하기
5. 그려보기 (시각화)
6. 이야기하고 다듬기 (반복)

## [프로젝트명] 도메인
[도메인 설명 - 비즈니스 컨텍스트와 핵심 개념들]

## 도메인 모델

### [엔티티명]
_Entity_
#### 속성
- `field1`: 설명
- `field2`: 설명

#### 행위
- `method1()`: 행위 설명
- `method2()`: 행위 설명

#### 규칙
- 비즈니스 규칙 1
- 비즈니스 규칙 2

### [Enum명]
_Enum_
#### 상수
- `VALUE1`: 설명
- `VALUE2`: 설명
```

**⚠️ 중요: 새로운 도메인 개념이 추가될 때마다 이 문서를 업데이트하세요.**

### 2. 용어사전.md 🔤

**도메인 용어의 한국어/영어 매핑 관리**

```markdown
# [프로젝트명] 용어 사전

| **한국어** | **영어**     | **설명** |
|---------|------------|--------|
| 회원      | Member     | 시스템 사용자 |
| 주문      | Order      | 구매 요청 |
| 상품      | Product    | 판매 대상 |
```

**🤔 용어 결정 규칙:**
- **모호한 용어 발견 시**: 개발자에게 질문하여 명확한 용어 결정
- **예시 질문**: "이 도메인에서 '주문'과 '구매'는 같은 의미인가요? 어떤 용어를 사용할까요?"
- **일관성 유지**: 한 번 결정된 용어는 프로젝트 전체에서 일관되게 사용

### 3. 개발가이드.md 🏗️

**아키텍처와 개발 규칙 정의**

```markdown
# [프로젝트명] 개발가이드

## 아키텍처
- 헥사고날 아키텍처
- 도메인 모델 패턴

### 계층
- Domain Layer
- Application Layer  
- Adapter Layer

> 외부(Actor) → 어댑터 → 애플리케이션 → 도메인

## 패키지
- domain
- application
  - provided (Use Case 인터페이스)
  - required (Repository 인터페이스)
- adapter
  - webapi
  - persistence
  - integration
  - security

## 구현 규칙
[프로젝트별 세부 규칙들]
```

---

## 🔄 개발 워크플로우

### 1. 초기 프로젝트 생성 시
1. **도메인 이해**: 요구사항 분석하여 핵심 도메인 개념 파악
2. **용어 정리**: 모호한 용어는 개발자에게 질문
3. **문서 생성**: 3개 필수 문서 생성
4. **코드 구현**: 아키텍처 가이드에 따라 구현

### 2. 기능 추가/변경 시
1. **도메인모델.md 업데이트**: 새로운 엔티티, 규칙 추가
2. **용어사전.md 업데이트**: 새로운 용어 추가/명확화
3. **코드 구현**: 기존 패턴 따라 구현
4. **문서 동기화**: 코드와 문서 일치 확인

### 3. 용어 모호성 처리
```
🤔 "이 기능에서 '등록'과 '가입'은 같은 의미인가요? 
   아니면 다른 단계를 의미하나요? 
   어떤 용어를 사용하시겠습니까?"

개발자 답변 후 → 용어사전.md 업데이트 → 코드에 일관되게 적용
```

---

## 🎯 사용 예시

**프롬프트 사용법:**
```
위의 가이드를 따라 "온라인 서점 관리 시스템"을 만들어주세요.

도메인: 고객이 책을 주문하고, 관리자가 재고를 관리하는 시스템
핵심 엔티티: 고객(Customer), 책(Book), 주문(Order), 재고(Inventory)

※ 모호한 용어가 있으면 질문해주세요.
```

**AI 응답 예시:**
```
🤔 몇 가지 용어를 명확히 하고 싶습니다:

1. "고객"과 "회원"을 구분하시나요? 
2. "주문"과 "구매"는 같은 의미인가요?
3. "재고"와 "인벤토리"는 어떤 용어를 선호하시나요?

답변해주시면 용어사전을 정리하고 일관된 코드를 작성하겠습니다.
```

---

## ✅ 체크리스트

### 프로젝트 완료 기준
- [ ] 헥사고날 아키텍처 구조 완성
- [ ] XML 기반 JPA 매핑 설정
- [ ] 도메인모델.md 작성 완료
- [ ] 용어사전.md 작성 완료  
- [ ] 개발가이드.md 작성 완료
- [ ] 각 계층별 테스트 코드 작성
- [ ] 문서와 코드 일치성 확인

### 지속적 관리
- [ ] 새 기능 추가 시 문서 업데이트
- [ ] 용어 일관성 검증
- [ ] 아키텍처 원칙 준수 확인
