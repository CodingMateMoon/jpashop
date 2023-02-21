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

    @ManyToOne(fetch = FetchType.LAZY) // ManyOrderToMember 다대일 여러 주문들이 하나의 멤버에 대응. order
    @JoinColumn(name = "member_id")
    private Member member;

    // JPQL select o From order o; -> SQL : select * from order 100 + 1(order). order 100개를 가져왔는데 member가 eager로 되어있으니 100번의 쿼리를 날립니다. N + 1(order) (order의 결과가 100개인데 그만큼 멤버를 가져오기 위해 100개의 쿼리가 더 날라갑니다)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
    persist(orderItemA)
    persist(orderItemB)
    persist(orderItemC)
    persist(order)

    cascade 활용 시 persist(order)만 사용하면 됩니다. casecade 시 연관된 persist를 전파합니다.
     */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1:1 양방향 관계 - 둘 다 FK를 둘 수 있는데 액세스를 많이 하는 Order에 FK를 둡니다.  (Delivery에도 둘 수는 있습니다)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // private Date의 경우 날짜 관련 어노테이션 매핑을 해야하는데 java8 LocalDateTime의 경우 하이버네이트가 알아서 처리해줍니다. order_date로 db 컬럼 생성

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    /*
    양방향 연관관계 Member가 주문을 하면 주문을 orders에 넣어줘야 합니다.
    public class Member {
        // MemberToOrder 하나의 회원이 여러 상품을 주문.
        @OneToMany(mappedBy = "member") // Order 객체의 member에 매핑됩니다.
        private List<Order> orders = new ArrayList<>();
    }
    */
    //==연관관계 편의 메서드==(핵심적으로 컨트롤하는 쪽에서 연관관계 메서드를 두면 좋습니다)//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /*
    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);
        order.setMember(member);
    }
     */
}
