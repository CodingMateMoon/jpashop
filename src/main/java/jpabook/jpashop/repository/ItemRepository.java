package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            /*
            item 새로 생성한 객체이기 때문에 처음에는 id값이 없어서 persist를 통해 신규로 등록합니다.
             */
            em.persist(item);
        } else {
            // id 값이 있을 경우 merge
            /* merge : DB에서 item을 가져온 뒤 set으로 값 설정하고 해당 item을 return하는 구조
            @Transactional
            public Item updateItem(Long itemId, Book param) {


                Item findItem = itemRepository.findOne(itemId);
                findItem.setPrice(param.getPrice());
                findItem.setName(param.getName());
                findItem.setStockQuantity(param.getStockQuantity());
                return findItem;
            }
            Item merge = em.merge(item);
            Item merge의 경우 병합이 돼서 영속성 컨테이너에서 관리하는 객체. 추가 작업이 필요한 경우 merge 객체로 작업
             */
            Item merge = em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
