package com.example.demo.model.service;

import java.util.List;
import java.util.Optional; // ⚠️ Optional<T> 사용을 위해 추가
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

    // ✅ 게시글 저장 메서드
    public Article save(AddArticleRequest request) {
        // DTO(AddArticleRequest)를 Article로 변환 후 저장
        return blogRepository.save(request.toEntity());
    }

    // ---------------------- 🔨 수정 기능 추가 ----------------------

    /**
     * 특정 ID의 게시글을 조회 (수정 화면으로 데이터 전달 시 사용)
     * @param id 게시글 ID
     * @return Optional<Article> (null 값 방지)
     */
    public Optional<Article> findById(Long id) { // 게시판 특정 글 조회
        return blogRepository.findById(id);
    }
    
    /**
     * 게시글을 수정하는 로직
     * @param id 수정할 게시글 ID
     * @param request 수정할 제목과 내용을 담은 DTO
     */
    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // 단일 글 조회
        
        // optionalArticle에 값이 존재할 경우에만 로직 실행
        optionalArticle.ifPresent(article -> { // 값이 있으면
            article.update(request.getTitle(), request.getContent()); // 값을 수정
            blogRepository.save(article); // Article 객체에 저장
        });
    }
    // ------------------------------------------------------------------

    // ----------------------  삭제 기능 추가 ----------------------
    
    /**
     * 특정 ID의 게시글을 삭제하는 로직
     * @param id 삭제할 게시글 ID
     */
    public void delete (Long id) { //
        blogRepository.deleteById(id); //
    }
    // ------------------------------------------------------------------
}