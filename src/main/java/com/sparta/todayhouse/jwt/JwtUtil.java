package com.sparta.todayhouse.jwt;

import com.sparta.todayhouse.repository.MemberRepository;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import com.sparta.todayhouse.shared.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.accessibility.AccessibleKeyBinding;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component                              //빈 객체에 등록해놓는 특정 annotation @Service 같은, 메모리 아끼는
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ACCESSKEY = "Authorization";
    private static final String REFRESHKEY = "refreshToken";
    private static final Long ACCESS_TOKEN_TIME = 60 * 60 *1000L;
    private static final Long REFRESH_TOKEN_TIME = 3 * 60 * 60 *1000L; // 3시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    Key key;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;   //알고리즘

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //토큰생성
    public String createToken(String email, String type) {
        long now = (new Date().getTime());

        long expiredTime = type.equals("Access")?  ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + expiredTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    //토큰 dto에 반환해주는 기능

    public TokenDto createAllToken(String email){
        return TokenDto.builder()
                .accessToken(createToken(email, "Access"))
                .refreshToken(createToken(email, "Refresh"))
                .build();
    }

    //token검사
    public Boolean tokenValidate(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){              //예외발생하면 무조건 요기로
            log.error(e.getMessage());
            return false;
        }
    }

    //시큐리티콘텍스홀더 인증객체 생성
    public Authentication getAuthentication(String email){
        UserDetailsImpl userDetails = userDetailsServiceImpl.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getTokenFromHeader(HttpServletRequest request, String type){
        switch(type){
            case "Access":
                String bearerToken = request.getHeader(ACCESSKEY);
                if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                    return bearerToken.substring(7);
                }
                else return null;

            case "Refresh":
                return request.getHeader(REFRESHKEY);

            default:
                return null;
        }
    }

    public String getEmailFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader(ACCESSKEY, BEARER_PREFIX + tokenDto.getAccessToken());
        response.addHeader(REFRESHKEY, tokenDto.getRefreshToken());
    }

}
