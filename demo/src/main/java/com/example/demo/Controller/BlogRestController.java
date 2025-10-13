package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody (JSON 반환)
public class BlogRestController {

    private final BlogService blogService;

    @PostMapping("/api/articles") // POST 요청 (글 추가)
    public ResponseEntity<Article> addArticle(@ModelAttribute AddArticleRequest request) {
        Article saveArticle = blogService.save(request); // 게시글 저장
        return ResponseEntity.status(HttpStatus.CREATED) // 상태코드 201 Created
                .body(saveArticle);
    }
}
