package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
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
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

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
