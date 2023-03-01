package jpabook.jpashop.domain;


import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter @Setter
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
