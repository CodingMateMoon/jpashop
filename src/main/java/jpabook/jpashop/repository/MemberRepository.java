package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

/* @SpringBootApplication이 있으면 해당 패키지와 패키지 하위에 있는 것들을 전부 컴포넌트 스캔해서 스프링 빈으로 자동 등록합니다.
@PersistenceContext가 있으면 스프링 JPA Entity 매니저를 주입해줍니다.
@PersistenceUnit을 통해 EntityManagerFactory를 주입받을 수도 있습니다.
 */

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    /*
    @PersistenceUnit
    private EntityManagerFactory emf;
     */

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // SQL은 테이블 대상 쿼리. 엔티티 객체 대상에 대해 alias m으로 주고 조회. from의 대상이 테이블이 아닌 Entity 입니다.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        // parameter name을 바인딩해서 특정 이름에 대한 회원 정보를 찾습니다.
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
