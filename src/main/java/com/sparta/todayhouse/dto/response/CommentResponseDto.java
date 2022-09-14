package com.sparta.todayhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private Long comment_id;
    private String profile_image;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isEditable;
}
