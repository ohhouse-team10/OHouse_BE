package com.sparta.todayhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostMainResponseDto {
    private Long post_id;
    private String profile_image;
    private String nickname;
    private String thumbnail;
}
