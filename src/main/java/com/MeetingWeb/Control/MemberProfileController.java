// MemberProfileController.java
package com.MeetingWeb.Control;

import com.MeetingWeb.Dto.MemberProfileDto;
import com.MeetingWeb.Service.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/start")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    // 페이지를 띄우기 위한 메서드
    @GetMapping("/mypage")
    public String viewMemberProfile(Principal principal , Model model) {
        //로그인할때 사용한 Id
        String userName = principal.getName();
        // 사용자 정보를 가져와서 모델에 추가
        model.addAttribute("memberProfileDto", memberProfileService.getMemberProfile(userName));

        // start/mypage.html 페이지로 이동합니다.
        return "start/mypage";
    }

    @PostMapping("/updateProfile")
    public String updateMemberProfile(@ModelAttribute MemberProfileDto memberProfileDto, Model model) {
        try {
            // 사용자 프로필 업데이트
            memberProfileService.updateMemberProfile(memberProfileDto);
            // 업데이트 후 마이페이지로 리다이렉트
            return "redirect:/start/mypage";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "사용자를 찾을 수 없습니다. 다시 시도해 주세요.");
            return "start/mypage";
        }
    }
}

