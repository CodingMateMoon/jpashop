package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorColumn("B") // single table 에서 저장할 때 db 구분값
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
