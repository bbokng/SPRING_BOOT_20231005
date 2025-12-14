package com.example.demo.Controller;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/join_new")
    public String join_new() {
        return "join_new";
    }

    // 회원가입 처리
    @PostMapping("/api/members")
    public String addmembers(@ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end";
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login";
    }

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "session_expired";
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
        try {
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());
            model.addAttribute("member", member);

            HttpSession existing = servletRequest.getSession(false);
            if (existing != null) {
                existing.invalidate();
            }
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            servletResponse.addCookie(cookie);

            HttpSession session = servletRequest.getSession(true);
            String sessionId = UUID.randomUUID().toString();
            String emailKey = "email:" + request.getEmail();
            String userKey = "userId:" + request.getEmail();
            session.setAttribute("userId", sessionId); // 기존 흐름 유지
            session.setAttribute("email", request.getEmail());
            session.setAttribute(userKey, sessionId);
            session.setAttribute(emailKey, request.getEmail());
            return "redirect:/board_list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/api/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        HttpSession newSession = request.getSession(true);
        log.info("새 세션 생성 후 attribute 확인: userId={}, email={}, keys={}",
                newSession.getAttribute("userId"), newSession.getAttribute("email"), newSession.getAttributeNames());

        return "redirect:/member_login";
    }
}
