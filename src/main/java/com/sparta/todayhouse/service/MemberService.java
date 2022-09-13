package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.MemberResponseDto;
import com.sparta.todayhouse.dto.response.ResponseDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.jwt.JwtUtil;
import com.sparta.todayhouse.jwt.TokenDto;
import com.sparta.todayhouse.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public ResponseDto<MemberResponseDto> signup(SignupRequestDto requestDto){
        Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());
        if(optionalMember.isPresent()){
            return ResponseDto.fail("DUPLICATED_NICKNAME","중복된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .build();

        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .updatedAt(member.getUpdatedAt())
                        .build()
        );
    }

    //로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "회원정보를 찾을 수 없습니다.");
        }
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "비밀번호가 올바르지 않습니다.");
        }

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        tokenToHeaders(tokenDto, response);


        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .updatedAt(member.getUpdatedAt())
                        .build()
        );
    }

    // 로그아웃
//    public ResponseDto<?> logout(HttpServletRequest request) {
////        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
////            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
////        }
////        Member member = tokenProvider.getMemberFromAuthentication();
//        if (null == member) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "사용자를 찾을 수 없습니다.");
//        }
//
//
//        return jwtUtil.deleteRefreshToken(member);
//    }


    //메서드 추가
    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    }



}
