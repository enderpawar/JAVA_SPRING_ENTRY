package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 서비스 클래스
 *
 * 이 클래스는 순수한 Java 클래스입니다.
 * Spring이 SpringConfig를 통해 Bean으로 등록하고 관리합니다.
 */
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 생성자를 통한 의존성 주입 (Constructor Injection)
     *
     * SpringConfig에서 new MemberService(memberRepository())를 호출할 때
     * memberRepository 객체가 이 생성자의 파라미터로 전달됩니다.
     *
     * 이것을 "의존성 주입(DI, Dependency Injection)"이라고 합니다.
     *
     * 왜 이렇게 할까요?
     * 1. 테스트하기 쉽다 - 다른 구현체로 쉽게 교체 가능
     * 2. 코드 재사용성이 높다
     * 3. 결합도가 낮아진다 (MemberService가 구체적인 Repository 구현을 몰라도 됨)
     */
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member){

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
               .ifPresent(m -> {
                  throw new IllegalStateException("이미 존재하는 회원입니다.");
               });
    }

    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
