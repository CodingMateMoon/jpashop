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
    public void updateItem(Long itemId, Book param) {
        /*
        변경 감지 기능 사용
        id 기반 실제 DB에 있는 영속 상태의 엔티티를 가져온 후 setter로 필드값을 설정합니다
        itemRepository.save(findItem) 같은 함수를 호출할 필요가 없습니다. findItem 영속 상태. 값을 세팅한 후 스프링 트랜잭션 commit이 되면 JPA는 flush를 날려서 영속성 컨텍스트에 있는 엔티티 중 변경된 것이 있는지 찾은 후 변경이 감지되면 바뀐 값을 update 쿼리를 통해 DB에 반영합니다.
         */
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
