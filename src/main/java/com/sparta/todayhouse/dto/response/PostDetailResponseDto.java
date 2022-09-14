package com.sparta.todayhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostDetailResponseDto {
    private String style;
    private String type;
    private String thumbnail;
    private String content;
    private String profile_image;
    private String nickname;
    private Boolean isFollow;
    private Boolean isLike;
    private int like_num;
    private Boolean isEditable;
}
