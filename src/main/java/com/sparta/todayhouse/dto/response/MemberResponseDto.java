package com.sparta.todayhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profile_image;
    private String status_message;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
}
