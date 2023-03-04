package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        /*
        delivery 따로 생성, OrderItem persist 후 세팅해줘야하는데 orderRepository.save(order); 하나만 했습니다.
        왜 이렇게 했냐면 cascade 옵션 때문입니다. orderItem cascade . order를 persist하면 컬렉션에 있는 orderItem도 persist를 날리고 delivery도 persist합니다. 그래서 하나만 저장해도 orderItem 및 delivery가 저장됩니다.
        Cascade를 어디까지 해야 하나?
        Order가 delivery, orderItem을 관리하는데 이 그림까지만 사용하고 참조하는게 주인이 private owner인 경우에 써야합니다. delivery , orderItem은 Order만 참조해서 씁니다.
        OrderItem이 다른 것을 참조할 수는 있지만 다른데서 OrderItem을 참조하는 곳이 없습니다. 라이프사이클을 동일하게 관리할 때 다른 것이 참조할 수 없는 private owner일 때 씁니다.
        그게 아니라면 예를 들어 Delivery가 중요해서 다른 엔티티에서도 참조해서 쓴다면 cascade를 쓰기 힘듭니다. 왜냐하면 다른 곳에서 Order 지울 때 다 지워질 수 있고 persist도 다른데 걸려있으면 복잡하게 얽힐 수 있습니다. 이런 경우 별도 repository를 생성해서 각각 persist를 하는게 좋습니다.

        Class Order {
        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
        private List<OrderItem> orderItems = new ArrayList<>();

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "delivery_id")
        private Delivery delivery;

        member, item 조회 후 배송정보 생성. OrderItem, Order static 생성 메서드를 통해 생성. 주문 저장할 때 cascade 옵션으로 OrderItem, Delivery는 자동으로 함께 persist 되면서 트랜잭션 커밋 시점에 flush가 일어나면서 insert가 DB에 날라갑니다.
         */

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        /* 다음과 같이 static 생성 메서드가 아닌 다른 형식으로 만들 경우 생성 메서드가 변경될 때 수정해야할 코드가 많아집니다.
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setCount();
        그래서 이러한 파편화를 막기 위해 생성자의 접근제한자를 protected로 변경합니다. (변경 시 new OrderItem(); 에서 컴파일 error 발생)
        class OrderItem {
            protected OrderItem() { == @NoArgsConstructor(access = AccessLevel.PROTECTED)

            }
         */

        // 주문 생성 ( parameter - CTRL + SPACE로 추천 자동 완성 활용)
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소 : Order.cancel
     public void cancel() {
         if (delivery.getStatus() == DeliveryStatus.COMP) {
         throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
         }
         this.setStatus(OrderStatus.CANCEL);
         for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
         }
     }

     주문 상품 취소 : OrderItem.cancel
     public void cancel() {
        getItem().addStock(count);
     }
     JPA 강점. 평상시 mybatis, jdbtemplate 등의 경우 데이터 변경한다음 바깥에서 업데이트 쿼리를 직접날려야합니다. 아이템의 재고도 plus하는 쿼리를 날려야합니다. 서비스 계층에서 데이터 수정 및 쿼리 비즈니스 로직을 직접 다 작성해야합니다. JPA를 활용할 경우 엔티티안의 데이터를 바꿨을 때 JPA가 알아서 변경 포인트 (더티 체킹. 변경 내역 감지)를 찾아서 데이터베이스에 업데이트 쿼리를 날립니다. this.setStatus(OrderStatus.CANCEL); ==> Order 변경, orderItem.cancel(); ==> OrderItem 변경
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    /* 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch)
    }
     */
}
