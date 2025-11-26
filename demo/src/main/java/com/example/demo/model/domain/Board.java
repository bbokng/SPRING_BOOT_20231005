package com.example.demo.model.domain;

import lombok.*; 
import jakarta.persistence.*; 

@Getter 
@Entity 
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false) 
    private Long id;

    @Column(name = "title", nullable = false) 
    private String title = "";

    @Column(name = "content", nullable = false)
    private String content = "";
    
    // ---------------------- ✅ 새로 추가된 필드 ----------------------
    
    @Column(name = "user", nullable = false) // 이름 [cite: 340]
    private String user = "";

    @Column(name = "newdate", nullable = false) // 날짜 [cite: 342]
    private String newdate = "";

    @Column(name = "count", nullable = false) // 조회수 [cite: 343]
    private String count = "";

    @Column(name = "likec", nullable = false) // 좋아요 [cite: 344]
    private String likec = "";
    
    // -------------------------------------------------------------

    @Builder // ✅ 새로운 필드들을 포함하도록 생성자 수정
    public Board(String title, String content, String user, String newdate, String count, String likec) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }

    // ---------------------- ✅ update 메서드 수정 ----------------------
    
    /**
     * 게시글의 제목, 내용 및 추가 필드를 업데이트하는 메서드
     */
    public void update (String title, String content, String user, String newdate, String count, String likec) { 
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }
    // ------------------------------------------------------------------
}