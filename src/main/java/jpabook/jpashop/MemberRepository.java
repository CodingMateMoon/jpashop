package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // 설정 파일 읽어서 엔티티 매니저 자동으로 생성
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        // CQRS 커맨드 쿼리 분리. 저장한다음 id 정도만 조회
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
