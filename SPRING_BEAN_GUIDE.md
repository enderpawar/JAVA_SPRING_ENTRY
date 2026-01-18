# 🎯 Spring Bean과 @Autowired 완벽 가이드

## 📚 목차
1. [Bean이란?](#bean이란)
2. [@Autowired란?](#autowired란)
3. [현재 프로젝트의 구조](#현재-프로젝트의-구조)
4. [실습 예제](#실습-예제)

---

## 🔷 Bean이란?

### 정의
**Spring IoC(Inversion of Control) 컨테이너가 관리하는 객체**입니다.

### 특징
- Spring이 **생성, 초기화, 소멸**을 관리
- 기본적으로 **싱글톤**(애플리케이션에서 1개만 생성)
- 필요할 때 **재사용**됨

### Bean 등록 방법

#### 방법 1: @Bean 사용 (현재 프로젝트 방식) ⭐
```java
@Configuration
public class SpringConfig {
    
    @Bean  // ← 이 메서드가 리턴하는 객체를 Bean으로 등록
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
```

#### 방법 2: @Component 스캔 (자동 등록)
```java
@Component  // 또는 @Service, @Repository, @Controller
public class MemberService {
    // Spring이 자동으로 Bean 등록
}
```

---

## 🔷 @Autowired란?

### 정의
**Spring 컨테이너에서 Bean을 찾아서 자동으로 주입**해주는 애노테이션입니다.

### 동작 원리
1. Spring이 `@Autowired`를 발견
2. 해당 타입의 Bean을 컨테이너에서 검색
3. 찾은 Bean을 자동으로 주입

### 사용 방법 3가지

#### ✅ 1. 생성자 주입 (가장 권장!)
```java
@Service
public class MemberService {
    private final MemberRepository repository;
    
    @Autowired  // 생성자가 1개면 생략 가능
    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }
}
```
**장점**: final 사용 가능, 불변성 보장, 테스트 용이

#### ⚠️ 2. Setter 주입 (선택적 의존성에만)
```java
@Service
public class MemberService {
    private MemberRepository repository;
    
    @Autowired
    public void setRepository(MemberRepository repository) {
        this.repository = repository;
    }
}
```
**단점**: final 불가, 객체가 불완전한 상태로 생성될 수 있음

#### ❌ 3. 필드 주입 (비추천!)
```java
@Service
public class MemberService {
    @Autowired
    private MemberRepository repository;
}
```
**단점**: 테스트 어려움, final 불가, Spring 없이 사용 불가

---

## 🔷 현재 프로젝트의 구조

### 📁 파일 구조
```
hello-spring/
├── SpringConfig.java           ← Bean 설정 (새로 추가됨)
├── service/
│   └── MemberService.java      ← 순수 Java 클래스
├── repository/
│   ├── MemberRepository.java   ← 인터페이스
│   └── MemoryMemberRepository.java  ← 구현체
└── test/
    └── SpringBeanExampleTest.java   ← Bean 주입 테스트
```

### 🔄 Bean 등록 흐름

```
1. Application 시작
   ↓
2. SpringConfig 스캔
   ↓
3. @Bean memberRepository() 실행
   → MemoryMemberRepository 객체 생성
   → Spring 컨테이너에 "memberRepository" 이름으로 저장
   ↓
4. @Bean memberService() 실행
   → memberRepository() 호출로 Bean 가져옴
   → MemberService 객체 생성 (Repository 주입)
   → Spring 컨테이너에 "memberService" 이름으로 저장
   ↓
5. Bean 등록 완료!
```

### 💉 의존성 주입(DI) 흐름

```java
// SpringConfig에서:
public MemberService memberService() {
    return new MemberService(memberRepository());  // ← DI 발생!
    //                       ↑
    //                       Repository Bean을 주입
}

// MemberService 생성자:
public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;  // ← 주입받은 Bean 저장
}
```

---

## 🔷 실습 예제

### 📝 SpringConfig.java (Bean 설정)
```java
@Configuration
public class SpringConfig {
    
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
```

### 📝 MemberService.java (Bean으로 등록됨)
```java
public class MemberService {
    private final MemberRepository memberRepository;
    
    // 생성자 주입 (DI)
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }
}
```

### 📝 테스트에서 @Autowired 사용
```java
@SpringBootTest
public class SpringBeanExampleTest {
    
    @Autowired  // ← Spring이 자동으로 Bean 주입
    private MemberService memberService;
    
    @Test
    public void 테스트() {
        // memberService는 이미 주입되어 있음!
        // null이 아니라 실제 객체
        assertNotNull(memberService);
    }
}
```

---

## 🎓 핵심 정리

### 🔑 5가지 핵심 개념

1. **Bean** = Spring이 관리하는 객체
2. **@Bean** = "이 객체를 Bean으로 등록해줘"
3. **@Autowired** = "Bean을 찾아서 자동으로 주입해줘"
4. **DI (의존성 주입)** = 객체를 외부에서 주입받는 것
5. **IoC (제어의 역전)** = 객체 생성/관리를 Spring에게 위임

### 📊 Before vs After

#### ❌ Before (직접 생성)
```java
public class MemberService {
    private MemberRepository repo = new MemoryMemberRepository();
    //                               ↑ 강한 결합!
    //                               변경 어려움, 테스트 어려움
}
```

#### ✅ After (의존성 주입)
```java
public class MemberService {
    private final MemberRepository repo;
    
    public MemberService(MemberRepository repo) {
        this.repo = repo;  // ← 외부에서 주입!
        //                    느슨한 결합, 테스트 쉬움
    }
}
```

### 🎯 왜 이렇게 복잡하게?

| 장점 | 설명 |
|-----|------|
| **싱글톤 관리** | Spring이 객체를 1개만 만들고 재사용 |
| **생명주기 관리** | 생성/초기화/소멸을 Spring이 알아서 |
| **느슨한 결합** | 인터페이스에 의존 → 구현체 교체 쉬움 |
| **테스트 용이** | Mock 객체로 쉽게 교체 가능 |
| **설정 중앙화** | SpringConfig에서 모든 Bean 관리 |

---

## 🧪 테스트 실행 방법

```bash
# Bean 주입 테스트 실행
./gradlew test --tests SpringBeanExampleTest
```

---

## 📚 추가 학습 자료

- `SpringConfig.java` - Bean 설정 예제
- `SpringBeanExampleTest.java` - @Autowired 동작 확인
- `AutowiredExamples.java` - @Autowired 3가지 방법
- `SpringBeanDiagram.java` - 시각적 다이어그램

---

## 💡 자주 하는 질문

### Q1: @Bean과 @Component의 차이?
- **@Bean**: 메서드에 사용, 명시적 Bean 등록
- **@Component**: 클래스에 사용, 자동 Bean 등록

### Q2: @Autowired는 어떻게 찾나?
1. 타입으로 먼저 검색 (MemberService 타입)
2. 같은 타입이 여러 개면 이름으로 검색
3. 못 찾으면 에러 (required=false면 null)

### Q3: 생성자 주입을 왜 권장?
- **final** 사용 가능 → 불변성
- **테스트** 용이 → new로 직접 생성 가능
- **순환 참조** 방지 → 컴파일 타임에 발견

### Q4: Spring 없이 테스트 가능?
```java
// 생성자 주입이면 가능!
MemberRepository repo = new MemoryMemberRepository();
MemberService service = new MemberService(repo);
// Spring 없이도 테스트 가능
```

---

## ✅ 체크리스트

- [ ] Bean이 무엇인지 이해했다
- [ ] @Bean과 @Autowired의 차이를 안다
- [ ] 의존성 주입(DI)의 개념을 이해했다
- [ ] 생성자 주입을 왜 권장하는지 안다
- [ ] SpringConfig의 역할을 이해했다
- [ ] 테스트 코드를 실행해봤다

---

**작성일**: 2026-01-18  
**버전**: 1.0

