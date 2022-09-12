package com.sparta.todayhouse.dto;

import com.sparta.todayhouse.shared.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseMessage<T> {
    private Boolean isSuccess;
    private T data;
    private String code;
    private String message;

    public ResponseMessage(Boolean isSuccess, T data, ErrorCode error){
        this.isSuccess = isSuccess;
        this.data = data;
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(true, data, ErrorCode.NULL);
    }

    public static <T> ResponseMessage<T> fail(ErrorCode error) {
        return new ResponseMessage<>(false, null, error);
    }
}
