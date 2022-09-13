package com.sparta.todayhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long post_id;
    private String profile_image;
    private String nickname;
    private Boolean isFollow;
    private String statusMessage;
    private String thumbnail;
    private Boolean isLike;
    private int like_num;
    private int comment_num;
    private String content;
}
