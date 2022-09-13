package com.sparta.todayhouse.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //에러x
    NULL("NO_ERROR", "NULL"),

    //로그인, 회원 가입 관련 에러
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일입니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "회원정보를 찾을 수 없습니다."),

    //POST(게시글) 관련 오류
    POST_NOT_FOUND("POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),

    //권한(토큰) 관련 오류
    NOT_AUTHORIZED("NOT_AUTHORIZED", "권한이 없습니다.");

//    //로그인(토큰) 관련 오류
//    NULL_TOKEN("NULL_TOKEN", "로그인이 필요합니다."),
//    INVALID_TOKEN("INVALID_TOKEN", "Token이 유효하지 않습니다."),
//    EXPIRED_TOKEN("EXPIRED_TOKEN", "만료된 토큰입니다."),
//    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
//
//    //회원가입 관련 오류
//    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일 입니다."),
//
//    //즐겨찾기 관련 오류
//    DUPLICATE_FAVORITE("DUPLICATE_FAVORITE", "이미 즐겨찾기에 등록되었습니다."),
//    FAVORITE_NOT_FOUND("FAVORITE_NOT_FOUND", "등록되지 않은 즐겨찾기입니다."),
//
//    //레스토랑(맛집) 관련 오류
//    RESTAURANT_NOT_FOUND("RESTAURANT_NOT_FOUND", "식당을 찾을 수 없습니다."),
//
//    //댓글 관련 오류
//    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다."),
//    INVALID_MEMBER("INVALID_MEMBER", "사용자가 일치하지 않습니다."),
//
//    //이미지 업로드 관련 오류
//    FAIL_TO_UPLOAD("FAIL_TO_UPLOAD", "이미지 업로드에 실패했습니다.");


    private final String code;
    private final String message;
}
