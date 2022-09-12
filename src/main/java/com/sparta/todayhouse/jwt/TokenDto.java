package com.sparta.todayhouse.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;

}
