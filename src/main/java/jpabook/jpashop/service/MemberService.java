package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
/**
 * JPA의 모든 데이터 변경이나 로직들은 @Transactional 안에서 실행되어야 합니다. 그래야 lazy 로딩 등이 가능합니다.
 * 기존에 스프링 관련 어노테이션을 사용하고 있고 스프링이 제공하는 트랜잭션에서 쓸 수 있는 옵션들이 많아서 스프링 트랜잭션을 권장합니다.
 * readOnly = true. JPA가 조회하는 곳에서 성능 최적화. 영속성 컨텍스트 flush 안하고 더티 체킹안하는 것의 이점도 있고 추가로 DB에 따라 읽기 전용 트랜잭션이면 리소스 많이 쓰지 말고
 * 읽기용 모드로 읽으라고 지시해주는 드라이버도 있습니다. 읽기에는 readOnly = true를 넣어주고 쓰기에는 넣어줄 경우 데이터 변경이 안됩니다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    // final로 선언 시 생성할 때 값을 초기화하지 않을 경우 컴파일 시점에서 오류를 반환해줍니다.
    private final MemberRepository memberRepository;

    /* 필드 injection의 경우 field를 바꿀 수 없는 단점이 있습니다.
    @Autowired
    private final MemberRepository memberRepository;

    setter injection의 경우 테스트 코드 작성할 때 가짜 MemberRepository를 주입해줄 수 있습니다. 하지만 실제 애플리케이션 구동 시점에 누군가가 바꿀 위험이 있습니다.
    보통 애플리케이션 로딩시점에 주입이 다 끝납니다. 세팅 조립이 다 끝난 이후에 바꿀일이 없기 때문에 setter injection은 별로 좋지 않습니다.
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    그래서 생성자 injection을 사용합니다.
    // 생성자가 하나만 있는 경우 자동으로 @Autowired 주입해줍니다.
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    테스트 케이스 작성 시에도 파라미터로 의존 객체를 넘길 수 있습니다.
    public static void main(String[] args) {
        MemberService memberService = new MemberService(MockMemberRepository());
    }
     */

    /**
     * 회원가입
     * @param member
     * @return
     */
    // 조회하는 함수가 많을 경우 class에 @Transactional(readOnly = true)를 기본적으로 적용하고 따로 저장이 필요한 트랜잭션에 @Transaction(readOnly = false)를 달아줍니다.
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        /*
        WAS가 동시에 여러 개 뜰 때 멤버 A 2개가 동시에 DB insert를 해서 동시에 validate 로직을 통과해서 회원가입을 두 번할 수 있습니다. 멀티 스레드 상황을 고려해서 Member의 name에 대해 uniq 제약 조건을 거는 것을 권장합니다.
         */
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
