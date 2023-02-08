package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;

    // No Entity Manager with actaul transaction available
    // org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource
    // Entity 매니저를 통한 모든 데이터 변경은 항상 트랜잭션안에서 이루어져야 합니다.
    @Test
    @Transactional
    @DisplayName("testMember")
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setUserName("memberA");
        // when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
    }

}