package com.sparta.todayhouse.jwt;

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
            try {
                if (jwtUtil.tokenValidate(accessToken)) {
                    String email = jwtUtil.getEmailFromToken(accessToken);
                    Authentication authentication = jwtUtil.getAuthentication(email);   //인증객체 생성
                    SecurityContextHolder.getContext().setAuthentication(authentication);   //저장
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
        }
        else{
            //refreshToken으로 검사하기
            if(StringUtils.hasText(refreshToken)){
                //refreshToken 유효성검사
                //검사해서 유효하면 accessToken 새로 발급해주기

            }
            else{
                request.setAttribute("exception", NULL_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

}
