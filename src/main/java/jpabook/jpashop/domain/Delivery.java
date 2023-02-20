package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

//    @Enumerated(EnumType.ORDINAL) // enum 필드에 대해 ORDINAL 숫자로 들어가는데 문제가 중간에 다른 상태가 생기면 망할 수 있습니다. 1 2 3 4 에서 2가 빠지거나 중간에 다른게 들어오는 경우 문제 발생
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP

}
