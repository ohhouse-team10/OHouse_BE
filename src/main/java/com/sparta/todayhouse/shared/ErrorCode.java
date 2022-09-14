package com.sparta.todayhouse.shared;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //에러x
    NULL("NO_ERROR", "에러 없음"),
    //예기치 못한 에러
    UNKNOWN_ERROR("UNKNOWN_ERROR", "예기치 못한 오류가 발생하였습니다."),

    //로그인, 회원 가입 관련 에러
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일입니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "회원정보를 찾을 수 없습니다."),

    //토큰 관련 오류
    NULL_TOKEN("NULL_TOKEN", "JWT 토큰을 찾지 못하였습니다."),
    INVALID_SIGNATURE("INVALID_SIGNATURE", "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", "지원하지 않는 JWT 토큰입니다."),
    INVALID_TOKEN("INVALID_TOKEN","잘못된 JWT 토큰입니다."),

    //Post(게시글) 관련 오류
    POST_NOT_FOUND("POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),

    //Comment(댓글) 관련 오류
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다."),

    //Favorite(즐겨찾기) 관련 오류
    DUPLICATE_LIKES("DUPLICATE_LIKES", "이미 좋아요를 눌렀습니다."),
    LIKES_NOT_FOUND("FAVORITE_NOT_FOUND", "아직 좋아요를 누르지 않았습니다."),

    //수정, 삭제권한 관련 오류
    NOT_AUTHORIZED("NOT_AUTHORIZED", "권한이 없습니다."),

    //이미지 업로드 관련 오류
    FAIL_TO_UPLOAD("FAIL_TO_UPLOAD", "이미지 업로드에 실패하였습니다.");


    private final String code;
    private final String message;
}
