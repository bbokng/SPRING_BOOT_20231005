package com.example.demo.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Article;
import com.example.demo.model.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성 (final 필드 주입)
public class BlogService {

    @Autowired // 객체 주입 자동화 (생성자 1개면 생략 가능)
    private final BlogRepository blogRepository; // 리포지토리 선언

    // ✅ 게시글 전체 목록 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // ✅ 게시글 저장 메서드 (여기를 추가!)
    public Article save(AddArticleRequest request) {
        // DTO(AddArticleRequest)를 Article로 변환 후 저장
        return blogRepository.save(request.toEntity());
    }
}

