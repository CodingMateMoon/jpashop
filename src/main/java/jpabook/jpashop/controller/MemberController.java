package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

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

    /*
    @Valid 사용시 MemberForm에 있는 @NotEmpty를 사용중인 것을 인지하고 validation을 적용해줍니다.
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result ) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        /* 예제용으로 단순하게 했지만 응답할 때 DTO를 반환하고 이를 바탕으로 화면에 출력하는게 좋습니다. 응답을 엔티티로 할 경우 엔티티에 로직을 추가했는데 API 스펙에 영향을 줄 수 있기 때문에 외부에는 절대 엔티티를 노출하지 않고 응답용 DTO를 따로 둡니다 */
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
