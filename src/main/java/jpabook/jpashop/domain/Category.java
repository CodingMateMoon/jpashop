package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //다대다 관계도 JoinTable로 중간 테이블 매핑이 필요합니다. 객체는 각 컬렉션이 있어서 다대다 관계가 가능한데 관계형 디비는 일대다 다대일로 풀어내는 중간 테이블(CATEGORY_ITEM)이 있어야 가능합니다. 실무에서는 이 정도까지만 가능한데 중간에 날짜등 필드를 더 추가할 수도 없고 실제로는 더 복잡한 경우가 많아서 사용하지 않습니다.
    @ManyToMany
    @JoinTable(name = "category_item", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
}
