package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping; // DELETE 매핑을 위해 추가

import lombok.RequiredArgsConstructor;
import com.example.demo.model.domain.Article;
import com.example.demo.model.service.BlogService;
import com.example.demo.model.service.AddArticleRequest; 


@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // ✅ 게시글 목록 보기
    @GetMapping("/article_list") // 게시판 링크 지정
    public String article_list(Model model) {
        List<Article> list = blogService.findAll(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // article_list.html 연결
    }

    // ✅ 게시글 수정 페이지로 이동
    @GetMapping("/article_edit/{id}") // 게시판 링크 지정
    public String article_edit(Model model, @PathVariable Long id) {
        Optional<Article> list = blogService.findById(id); // 선택한 게시글 조회

        if (list.isPresent()) {
            model.addAttribute("article", list.get()); // 존재하면 모델에 추가
        } else {
     
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "article_edit"; // article_edit.html로 연결
    }

    // ----------------------  수정 요청 처리 기능 (PUT 매핑) ----------------------
    
    /**
     * 게시글 수정 요청을 처리하고, BlogService의 update 메서드를 호출합니다.
     * @param id 수정할 게시글 ID (Path Variable)
     * @param request 수정 내용을 담은 DTO (ModelAttribute)
     * @return 게시글 목록 페이지로 리다이렉트
     */
    @PutMapping("/api/article_edit/{id}") // PUT 매핑 추가
    public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        
        blogService.update(id, request); // 수정 서비스 로직 호출
        
        return "redirect:/article_list"; // 글 수정 이후 목록 페이지로 리다이렉트
    }
    // -------------------------------------------------------------------------
    
    // ---------------------- 삭제 요청 처리 기능 (DELETE 매핑) ----------------------
    
    /**
     * 게시글 삭제 요청을 처리하고, BlogService의 delete 메서드를 호출합니다.
     * @param id 삭제할 게시글 ID (Path Variable)
     * @return 게시글 목록 페이지로 리다이렉트
     */
    @DeleteMapping("/api/article_delete/{id}") // DELETE 매핑 추가
    public String deleteArticle(@PathVariable Long id) {
        
        blogService.delete(id); // BlogService의 delete 메서드 호출
        
        return "redirect:/article_list"; // 글 삭제 이후 목록 페이지로 리다이렉트
    }
    // ----------------------------------------------------------------------------

    // ✅ 파비콘 요청 무시용 (브라우저가 자동으로 /favicon.ico 요청할 때 오류 방지)
    @GetMapping("/favicon.ico")
    public void favicon() {
        // 아무 작업도 하지 않음
    }
}