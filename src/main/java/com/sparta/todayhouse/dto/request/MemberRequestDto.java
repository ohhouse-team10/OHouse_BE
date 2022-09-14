package com.sparta.todayhouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String nickname;
    private String profile_image;
    private String status_message;

}
