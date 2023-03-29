package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        /* setter를 쓰기 보다 create book 등    생성자 메서드를 활용하여 만들고 넘겨주는 것이 좋습니다.*/
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    // id가 조작돼서 넘어올 경우 다른 사람것이 수정될 수 있습니다. 이러한 취약점을 해결하기 위해 이 유저가 이 아이템에 대해 권한이 있는지 서버에서 체크하는 로직, 업데이트할 객체를 세션에 담아두고 풀어내는 방법 등이 있습니다.
    /*
    updateItemForm에서 데이터 입력 후 submit. new를 통해 새로 생성한 객체에 id 세팅된 것. JPA에 한번 들어갔다 나온 객체. 식별자가 있는 경우 준영속 상태의 객체라고 합니다.
    데이터베이스 한번 저장된 후 가져온 것이라 JPA가 식별할 수 있는 ID를 가지고 있고 영속성 컨텍스트가 더는 관리하지 않는 준영속 엔티티입니다.
    JPA가 관리하는 영속 상태 엔티티는 뭐가 변경되었는지 JPA가 보다가 변경 감지가 일어나지만 준영속 엔티티는 JPA가 관리하지 않아서 Book을 바꿔도 DB에 변경이 일어나지 않습니다. JPA가 관리하지 않아서 업데이트를 할 수 있는 근거 자체가 없습니다.
     */
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        /*
        Book book= new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        어설프게 엔티티를 파라미터로 쓰기보다 필요한 데이터만 받도록 수정합니다. 업데이트할 데이터가 많을 경우 서비스 계층의 DTO를 하나 만듭니다.
        */
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
