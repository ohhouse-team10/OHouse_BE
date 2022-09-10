package com.sparta.todayhouse.dto.request;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String email;
    private String password;
    private String nickname;
}
