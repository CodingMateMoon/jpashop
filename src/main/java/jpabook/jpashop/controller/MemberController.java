package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
    MemberForm 껍데기를 model에 넘기고 화면에서 활용합니다. th:object="${memberForm}" , th:field="*{name}" : * 표시는 object를 참고해서 해당 property에 접근합니다.
    th:field로 적으면 id="name", name="name" 형식으로 렌더링될 때 thymeleaf가 만들어줍니다.
    객체 validation에 사용됩니다.
     */
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }
}
