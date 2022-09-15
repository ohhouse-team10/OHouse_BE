package com.sparta.todayhouse.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorCode exception = (ErrorCode) request.getAttribute("exception");

        if(null != exception) setResponse(response, exception);
    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().print(
                new ObjectMapper().writeValueAsString(
                        ResponseMessage.fail(errorCode)
                )
        );
    }
}
