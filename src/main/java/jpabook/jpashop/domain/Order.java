package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // ManyOrderToMember 다대일 여러 주문들이 하나의 멤버에 대응. order
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private Delivery delivery;

    private LocalDateTime orderDate; // private Date의 경우 날짜 관련 어노테이션 매핑을 해야하는데 java8 LocalDateTime의 경우 하이버네이트가 알아서 처리해줍니다.

    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
