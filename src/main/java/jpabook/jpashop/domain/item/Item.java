package jpabook.jpashop.domain.item;


import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // SINGLE_TABLE, JOINED 정규화된 스타일, TABLE_PER_CLASS - book movie album 3개 테이블이 다 나오는 전략
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    /* 도메인 주도 설계에서 엔티티 자체가 해결할 수 있는 것들은 주로 엔티티안에 비즈니스 로직을 넣습니다. 객체지향적으로 stockQuantity 재고수량이 Item 엔티티 안에 있기 때문에 이 데이터를 가지고 있는 곳에서 비즈니스 로직을 수행하는 게 응집도가 있습니다.
    과거에 아이템 서비스에서 stock 가져와서 더해서 넣고 값을 만든다음 item.setStock() 해서 결과를 넣었다면 객체지향적으로 생각했을 때 데이터를 가지고 있는 쪽이 비즈니스 메서드를 가지고 있는 것이 좋습니다. setter를 사용하지 않고 엔티티안에서 비즈니스 로직으로 수정합니다.
     */

    /**
     * stock 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
