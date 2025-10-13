package com.example.demo.Controller;


import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    
    @Autowired
    private TestService TestService;

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", "방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }
   
    @GetMapping("/about_detailed")
    public String about() {
        return "about_detailed";
    }

    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {
    model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
    model.addAttribute("data2", "태그의 속성 값");
    model.addAttribute("link", 01);
    model.addAttribute("name", "홍길동");
    model.addAttribute("para1", "001");
    model.addAttribute("para2", 002);
    return "thymeleaf_test1";
    }

     @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        // name 컬럼에서 "홍길동"을 찾음
        TestDB test = TestService.findByName("홍길동");

        // Thymeleaf로 전달
        model.addAttribute("data4", test);

        // 디버깅 로그
        System.out.println("데이터 출력 디버그 : " + test);

        // templates/testdb.html 반환
        return "testdb";
    }

    
}
