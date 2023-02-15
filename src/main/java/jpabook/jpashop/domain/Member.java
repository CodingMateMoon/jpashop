package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded // 내장 타입 쓸 때 사용 (@Embeddable 둘 중 하나만 있어도 됐는데 알아보기 쉽게 명시해줍니다)
    private Address address;

    // MemberToOrder하나의 회원이 여러 상품을 주문
    @OneToMany
    private List<Order> orders = new ArrayList<>();
}
