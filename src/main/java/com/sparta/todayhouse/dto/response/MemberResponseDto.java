package com.sparta.todayhouse.dto.response;

import com.sparta.todayhouse.shared.Role;
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
    private Role role;
    private String nickname;
    private String profile_image;
    private String status_message;
}
