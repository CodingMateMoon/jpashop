package jpabook.jpashop.domain;


import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "order_id")
    private Order order;
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    /* JPA는 protected까지 기본 생성자를 만들 수 있도록 허용합니다. 직접 생성하는 게 아니라 다른 방식으로 생성해야 하는 것을 명시
    protected OrderItem() { == @NoArgsConstructor(access = AccessLevel.PROTECTED)

    }
     */

    //==생성 메서드==// Item, 쿠폰 할인 등으로 orderPrice가 변경될 수 있기 때문에 따로 둡니다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직==// 재고 수량을 원복해줍니다.
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     * @re품urn
     */

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
