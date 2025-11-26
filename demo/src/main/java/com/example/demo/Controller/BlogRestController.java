package com.example.demo.Controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BlogRestController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Board> addArticle(@ModelAttribute AddArticleRequest request) {
        Board savedBoard = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }
}
