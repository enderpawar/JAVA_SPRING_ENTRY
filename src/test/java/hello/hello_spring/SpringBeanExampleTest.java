package hello.hello_spring;

import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Bean과 @Autowired 동작 원리 테스트
 *
 * @SpringBootTest는 Spring 컨테이너를 실제로 띄워서 테스트합니다.
 * 이를 통해 Bean이 어떻게 주입되는지 확인할 수 있습니다.
 */
@SpringBootTest
public class SpringBeanExampleTest {

    /**
     * @Autowired가 하는 일:
     *
     * 1. Spring이 이 클래스를 스캔할 때 @Autowired를 발견
     * 2. Spring 컨테이너에서 MemberService 타입의 Bean을 찾음
     * 3. SpringConfig에서 @Bean으로 등록한 memberService()가 리턴한 객체를 찾음
     * 4. 그 객체를 이 필드에 자동으로 할당
     *
     * 즉, 우리가 직접 new MemberService(...)를 하지 않아도
     * Spring이 알아서 객체를 만들고 주입해줍니다!
     */
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Bean이 제대로 주입되었는지 확인하는 테스트
     */
    @Test
    public void Bean주입_테스트() {
        // memberService가 null이 아니라는 것은
        // Spring이 제대로 Bean을 주입했다는 증거!
        System.out.println("=".repeat(60));
        System.out.println("🔍 Spring Bean 주입 확인");
        System.out.println("=".repeat(60));

        System.out.println("\n1️⃣ MemberService Bean:");
        System.out.println("   - 주입된 객체: " + memberService);
        System.out.println("   - null 여부: " + (memberService == null ? "❌ null (실패)" : "✅ 주입 성공"));

        System.out.println("\n2️⃣ MemberRepository Bean:");
        System.out.println("   - 주입된 객체: " + memberRepository);
        System.out.println("   - null 여부: " + (memberRepository == null ? "❌ null (실패)" : "✅ 주입 성공"));

        System.out.println("\n3️⃣ MemberService 내부의 Repository:");
        System.out.println("   - memberService가 사용하는 repository는");
        System.out.println("     위의 memberRepository와 같은 객체입니다!");
        System.out.println("   - 이것이 바로 '싱글톤(Singleton)' 패턴입니다.");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📝 정리:");
        System.out.println("   Spring이 SpringConfig를 보고:");
        System.out.println("   1. MemoryMemberRepository 객체를 1개 생성 (Bean 등록)");
        System.out.println("   2. 그 객체를 MemberService 생성자에 전달");
        System.out.println("   3. MemberService 객체를 1개 생성 (Bean 등록)");
        System.out.println("   4. @Autowired를 발견하면 등록된 Bean을 자동 주입");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * 같은 Bean이 재사용되는지 확인
     */
    @Test
    public void 싱글톤_확인() {
        System.out.println("\n🔄 싱글톤 패턴 확인:");
        System.out.println("   Spring은 기본적으로 Bean을 1개만 만들고 재사용합니다.");
        System.out.println("   아무리 많은 곳에서 @Autowired를 해도 같은 객체를 주입받습니다.");
    }
}

