package com.sparta.todayhouse.shared;

import com.sparta.todayhouse.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    public static ResponseEntity<?> returnResponse(ResponseMessage<?> data){
        if(data.getIsSuccess()) return ResponseEntity.ok().body(data);
        else{
            switch(data.getCode()){
                //400 : 잘못된 문법(ex. data form)
                case NULL_TOKEN:
                case DUPLICATE_EMAIL:
                case DUPLICATE_LIKES:
                    return new ResponseEntity(data, HttpStatus.BAD_REQUEST);
                //401 : 인증이 필요함(ex. 로그인/토큰)
                case INVALID_SIGNATURE:
                case EXPIRED_TOKEN:
                case UNSUPPORTED_TOKEN:
                case INVALID_TOKEN:
                    return new ResponseEntity(data, HttpStatus.UNAUTHORIZED);
                //403 : 접근 권한 없음
                case NOT_AUTHORIZED:
                    return new ResponseEntity(data, HttpStatus.FORBIDDEN);
                //404 : 리소스가 없음
                case MEMBER_NOT_FOUND:
                case FILE_NOT_FOUND:
                case POST_NOT_FOUND:
                case COMMENT_NOT_FOUND:
                case LIKES_NOT_FOUND:
                    return new ResponseEntity(data, HttpStatus.NOT_FOUND);
                //415 : 포맷을 지원하지 않음
                case FAIL_TO_UPLOAD:
                    return new ResponseEntity(data, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        }

        //500 : 알수 없는 서버 에러
        return new ResponseEntity(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
