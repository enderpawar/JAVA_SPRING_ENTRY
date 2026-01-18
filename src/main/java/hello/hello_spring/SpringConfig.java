package hello.hello_spring;

import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Bean 설정 클래스
 *
 * 이 클래스는 Spring에게 "어떤 객체들을 Bean으로 만들어서 관리할지" 알려줍니다.
 */
@Configuration  // Spring에게 "이것은 설정 파일이야"라고 알려줌
public class SpringConfig {

    /**
     * MemberRepository Bean 등록
     *
     * @Bean 어노테이션이 붙은 메서드는 Spring이 실행해서
     * 리턴되는 객체를 Spring 컨테이너에 Bean으로 등록합니다.
     *
     * 이렇게 하면:
     * 1. Spring 컨테이너가 이 메서드를 호출
     * 2. new MemoryMemberRepository() 객체를 생성
     * 3. Spring 컨테이너에 "memberRepository"라는 이름으로 저장
     * 4. 다른 곳에서 MemberRepository가 필요하면 이 객체를 재사용
     */
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    /**
     * MemberService Bean 등록
     *
     * 이 메서드가 실행될 때:
     * 1. Spring이 memberRepository() 메서드를 먼저 호출해서 Bean을 가져옴
     * 2. 그 Bean을 MemberService 생성자에 주입 (DI - Dependency Injection)
     * 3. MemberService 객체를 Bean으로 등록
     *
     * 주의: 파라미터로 받는 memberRepository는
     *       위의 @Bean memberRepository() 메서드가 리턴한 그 객체입니다!
     */
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}

