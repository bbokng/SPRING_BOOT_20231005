package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BlogRepository;
import com.example.demo.model.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BoardRepository boardRepository;

    // ---------------------- 기존 Article 기능 ----------------------
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Optional<Article> findById(Long id) {
        return blogRepository.findById(id);
    }

    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id);
        optionalArticle.ifPresent(article -> {
            article.update(request.getTitle(), request.getContent());
            blogRepository.save(article);
        });
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    // ---------------------- Board 기능 ----------------------
    public List<Board> findAllBoard() {
        return boardRepository.findAll();
    }

    public Optional<Board> findBoardById(Long id) {
        return boardRepository.findById(id);
    }

    public void updateBoard(Long id, AddArticleRequest request) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        optionalBoard.ifPresent(board -> {
            board.update(
                    request.getTitle(),
                    request.getContent(),
                    board.getUser(),
                    board.getNewdate(),
                    board.getCount(),
                    board.getLikec()
            );
            boardRepository.save(board);
        });
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    public Board save(AddArticleRequest request) {
        return boardRepository.save(request.toEntity());
    }

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }
}
