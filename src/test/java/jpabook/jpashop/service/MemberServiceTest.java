package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/*
순수 단위 테스트가 아니라 JPA가 DB에 도는 것을 보기 위해 메모리 모드로 DB까지 다 엮어서 테스트를 합니다. spring과 integration 해서 테스트합니다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    /* @Test에 @Transactional이 있으면 기본적으로 테스트 후 롤백합니다.
    @Rollback(false) : 롤백하지 않도록 설정합니다.

    em.persist(member) : persist를 해도 insert는 실행되지 않습니다. database 트랜잭션이 커밋될 때 flush 되면서 db에 insert 됩니다.
     */
    public void 회원가입() throws Exception{
        // given - 이런게 주어졌을 때 이렇게 하면 이런 결과가 나옵니다 형식의 테스트
        Member member = new Member();
        member.setName("kim");
        // when
        Long savedId = memberService.join(member);
        // then
        em.flush(); // 영속성 컨텍스트 등록 내용을 데이터베이스에 반영합니다.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2);
        /*
        @Test(expected = IllegalStateException.class) 설정으로 IllegalStateException이 발생했을 때 테스트가 성공되도록 설정합니다.

        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }
         */

        // then
        // Exception이 발생해야하는 상황에서 발생하지 않으면 의도한 바와 다르게 동작하는 것입니다. 이와 같은 상황을 막기 위해 Exception없이 지나칠 경우 테스트가 fail되도록 처리합니다.
        fail("예외가 발생해야 한다.");
    }


}