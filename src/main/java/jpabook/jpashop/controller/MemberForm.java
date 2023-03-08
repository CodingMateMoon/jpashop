package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    /*
    Member 객체를 쓸 수도 있지만 화면에서 넘어올 때 validation과 도메인이 원하는 validation이 다를 수 있기에 화면에서 필요한 Form 객체를 따로 분리하는 것을 권장합니다.
     */
    @NotEmpty(message = "회원 이름은 필수입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
