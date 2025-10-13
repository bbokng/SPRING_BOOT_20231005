package com.example.demo.Controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import com.example.demo.model.domain.Article;
import com.example.demo.model.service.BlogService;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/article_list") // 게시판 링크 지정
    public String article_list(Model model) {
        List<Article> list = blogService.findAll(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // article_list.html 연결
    }

    // ✅ 파비콘 요청 무시용 (브라우저가 자동으로 /favicon.ico 요청할 때 오류 방지)
    @GetMapping("/favicon.ico")
    public void favicon() {
        // 아무 작업도 하지 않음
    }
}
