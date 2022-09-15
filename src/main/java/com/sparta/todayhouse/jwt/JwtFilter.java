package com.sparta.todayhouse.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todayhouse.dto.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sparta.todayhouse.shared.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String accessToken = jwtUtil.getTokenFromHeader(request, "Access");
        String refreshToken = jwtUtil.getTokenFromHeader(request, "Refresh");

        if(StringUtils.hasText(accessToken)){
            if(checkToken(request, accessToken)) createAuthentication(accessToken);
        }
        else{
            //refreshToken으로 검사하기
            if(StringUtils.hasText(refreshToken)){
                //refreshToken 유효성검사
                if(checkToken(request, refreshToken)){
                    //검사해서 유효하면 accessToken 새로 발급해주기
                    String email = jwtUtil.getEmailFromToken(refreshToken);
                    String token = jwtUtil.createToken(email, "Access");
                    jwtUtil.tokenToHeaders(TokenDto.builder()
                            .accessToken(token)
                            .refreshToken(refreshToken)
                            .build(), response);

                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(
                            new ObjectMapper().writeValueAsString(
                                    ResponseMessage.success("access-token 재발급")
                            )
                    );
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            else{
                //access, refresh 둘 다 없을 경우
                request.setAttribute("exception", NULL_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void createAuthentication(String token){
        String email = jwtUtil.getEmailFromToken(token);
        Authentication authentication = jwtUtil.getAuthentication(email);   //인증객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication);   //저장
    }

    private Boolean checkToken(HttpServletRequest request, String token){
        try {
            if (jwtUtil.tokenValidate(token)) {
                return true;
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", INVALID_TOKEN);
        } catch (Exception e) {
            request.setAttribute("exception", UNKNOWN_ERROR);
        }

        return false;
    }

}
