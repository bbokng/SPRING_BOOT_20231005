package com.example.demo.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.TestDB;
import com.example.demo.model.repository.TestRepository;
import lombok.RequiredArgsConstructor;

@Service // 서비스 등록, 스프링 내부 자동 등록됨
@RequiredArgsConstructor // final 필드 기반 생성자 자동 생성
public class TestService {
    @Autowired
    private final TestRepository testRepository; // 생성자 주입됨

    public TestDB findByName(String name) { // 이름 찾기
        return testRepository.findByName(name);
    }
}
