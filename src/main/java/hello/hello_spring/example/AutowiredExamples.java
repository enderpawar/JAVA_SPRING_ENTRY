package hello.hello_spring.example;

import hello.hello_spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Autowired 사용법 3가지 예제
 *
 * 이 파일은 학습용 예제입니다. 실제로는 사용하지 않습니다.
 */
public class AutowiredExamples {

    // ============================================
    // 방법 1️⃣: 생성자 주입 (Constructor Injection) ⭐ 가장 권장!
    // ============================================
    @Service
    public static class Example1_ConstructorInjection {

        private final MemberRepository memberRepository;

        /**
         * 생성자 주입 - 가장 권장되는 방법!
         *
         * 장점:
         * 1. final 키워드 사용 가능 → 불변성 보장
         * 2. 테스트하기 쉬움 (new로 직접 생성 가능)
         * 3. 순환 참조 방지
         * 4. Spring 4.3부터는 생성자가 1개면 @Autowired 생략 가능!
         *
         * Spring이 하는 일:
         * - "Example1_ConstructorInjection Bean을 만들어야겠네?"
         * - "생성자를 보니 MemberRepository가 필요하구나!"
         * - "컨테이너에서 MemberRepository Bean을 찾아서 전달해주자!"
         */
        @Autowired  // 생성자가 1개면 생략 가능
        public Example1_ConstructorInjection(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
            System.out.println("✅ 생성자 주입 완료!");
        }
    }

    // ============================================
    // 방법 2️⃣: Setter 주입 (Setter Injection) ⚠️ 선택적 의존성에만 사용
    // ============================================
    @Service
    public static class Example2_SetterInjection {

        private MemberRepository memberRepository;

        /**
         * Setter 주입
         *
         * 단점:
         * 1. final 사용 불가 → 가변 객체
         * 2. 객체 생성 후 의존성이 나중에 주입됨 → NPE 위험
         * 3. 의존성이 선택적일 때만 사용 권장
         *
         * Spring이 하는 일:
         * - "Example2_SetterInjection 객체를 먼저 생성 (기본 생성자)"
         * - "그 다음에 setMemberRepository() 메서드를 호출"
         * - "MemberRepository Bean을 파라미터로 전달"
         */
        @Autowired
        public void setMemberRepository(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
            System.out.println("✅ Setter 주입 완료!");
        }
    }

    // ============================================
    // 방법 3️⃣: 필드 주입 (Field Injection) ❌ 비추천!
    // ============================================
    @Service
    public static class Example3_FieldInjection {

        /**
         * 필드 주입 - 간단하지만 비추천!
         *
         * 단점:
         * 1. final 사용 불가
         * 2. 테스트하기 어려움 (Reflection 사용)
         * 3. 순환 참조 감지 안됨
         * 4. Spring 없이는 사용 불가
         *
         * 코드는 간단해 보이지만 실무에서는 사용하지 마세요!
         *
         * Spring이 하는 일:
         * - "Example3_FieldInjection 객체를 먼저 생성"
         * - "Reflection을 사용해서 private 필드에 강제로 값 주입"
         */
        @Autowired
        private MemberRepository memberRepository;

        public Example3_FieldInjection() {
            System.out.println("⚠️ 필드 주입 - 비추천 방식!");
        }
    }

    // ============================================
    // 추가: @Autowired의 옵션들
    // ============================================
    @Service
    public static class Example4_AutowiredOptions {

        private MemberRepository memberRepository;

        /**
         * required = false: Bean이 없어도 에러 안남 (null 주입)
         *
         * 기본적으로 @Autowired는 required = true라서
         * Bean을 찾지 못하면 에러가 발생합니다.
         *
         * 선택적 의존성일 때만 사용하세요.
         */
        @Autowired(required = false)
        public void setOptionalDependency(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
            if (memberRepository == null) {
                System.out.println("⚠️ Bean을 찾지 못했지만 에러는 안남");
            }
        }
    }
}

