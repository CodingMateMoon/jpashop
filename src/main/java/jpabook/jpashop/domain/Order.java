package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne // ManyOrderToMember 다대일 여러 주문들이 하나의 멤버에 대응
    @JoinColumn(name = "member_id")
    private Member member;
}
