package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    /*
     ItemService는 단순하게 ItemRepository에 위임만 하는 서비스입니다.
     경우에 따라서는 위임만 하는 경우 만들 필요가 있을까? 컨트롤러에서 repository 바로 접근해서 써도 크게 문제가 없다고 생각합니다.
     */

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        /*
        변경 감지 기능 사용
        id 기반 실제 DB에 있는 영속 상태의 엔티티를 가져온 후 setter로 필드값을 설정합니다
        itemRepository.save(findItem) 같은 함수를 호출할 필요가 없습니다. findItem 영속 상태. 값을 세팅한 후 스프링 트랜잭션 commit이 되면 JPA는 flush를 날려서 영속성 컨텍스트에 있는 엔티티 중 변경된 것이 있는지 찾은 후 변경이 감지되면 바뀐 값을 update 쿼리를 통해 DB에 반영합니다.

        merge를 할 경우 데이터 필드값들을 전부 갈아치우기 때문에 필드값 중 하나라도 setPrice(price)와 같이 값을 세팅안할 경우 price=null로 바꿔치기가 될 수 있습니다. 따라서 merge보다는 필요한 필드만 수정하는 변경 감지를 하는 것이 좋습니다.
         */
        //setter보다 의미 있는 메서드를 통해 변경해야 추적하기 좋습니다 : findItem.change(price, name, stockQuantity);

        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
