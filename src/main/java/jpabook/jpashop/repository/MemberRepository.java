package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/* @SpringBootApplication이 있으면 해당 패키지와 패키지 하위에 있는 것들을 전부 컴포넌트 스캔해서 스프링 빈으로 자동 등록합니다.
@PersistenceContext가 있으면 스프링 JPA Entity 매니저를 주입해줍니다.
@PersistenceUnit을 통해 EntityManagerFactory를 주입받을 수도 있습니다.
 */

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
    @PersistenceContext
    private EntityManager em;
     */
    // EntityManager는 @PersistenceContext 표준 어노테이션이 있어야 injection이 되는데 스프링 부트가 injection되도록 지원해줍니다. spring data jpa가 없으면 지원하지 않습니다.
    // 버전에 따라 스프링 부트 기본 라이브러리에서도 @Autowired로 EntityManager 주입을 지원하도록 지원합니다.
    private final EntityManager em;

    /*
    @PersistenceUnit
    private EntityManagerFactory emf;
     */

    /**
     * em.persist를 하면 영속성 컨텍스트에 member 객체를 올립니다. 영속성 컨텍스트에는 key value가 있는데 member 객체의 id값이 key가 됩니다. @GeneratedValue를 하면 id가 항상 생성되는 것이 보장돼서 영속성 컨텍스트에 해당 id가 key로 들어갑니다.
     * @param member
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
