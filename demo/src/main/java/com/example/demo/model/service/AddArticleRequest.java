package com.example.demo.model.service;

import com.example.demo.model.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddArticleRequest {
    private String title;
    private String content;
    private String user;
    private String newdate;
    private String count;
    private String likec;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .newdate(newdate)
                .count(count)
                .likec(likec)
                .build();
    }
}
