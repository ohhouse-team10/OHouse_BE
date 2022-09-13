package com.sparta.todayhouse.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String accessToken = jwtUtil.getTokenFromHeader(request, "Access");
        String refreshToken = jwtUtil.getTokenFromHeader(request, "Refresh");

        if(accessToken == null){

        }else {
            if(jwtUtil.tokenValidate(accessToken)){
                String email = jwtUtil.getEmailFromToken(accessToken);
                Authentication authentication = jwtUtil.getAuthentication(email);   //인증객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication);   //저장
                }
            else{


            }
        }

        filterChain.doFilter(request, response);}

    //헤더에서 토큰 가져오기



}
