package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.LoginResponseDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.jwt.JwtUtil;
import com.sparta.todayhouse.jwt.TokenDto;
import com.sparta.todayhouse.repository.MemberRepository;
import com.sparta.todayhouse.shared.Role;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.todayhouse.shared.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public ResponseMessage<?> signup(SignupRequestDto requestDto){
        Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());
        if(optionalMember.isPresent()){
            return ResponseMessage.fail(DUPLICATE_EMAIL);
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .profile_image("")
                .status_message("")
                .build();

        memberRepository.save(member);
//        return ResponseDto.success(
//                MemberResponseDto.builder()
//                        .id(member.getId())
//                        .email(member.getEmail())
//                        .nickname(member.getNickname())
//                        .createdAt(member.getCreatedAt())
//                        .updatedAt(member.getUpdatedAt())
//                        .build()
        return ResponseMessage.success("signup success");
    }

    //로그인
    @Transactional
    public ResponseMessage<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null == member) {
            return ResponseMessage.fail(MEMBER_NOT_FOUND);
        }
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseMessage.fail(MEMBER_NOT_FOUND);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        jwtUtil.tokenToHeaders(tokenDto, response);

        return ResponseMessage.success(LoginResponseDto.builder()
                .nickname(member.getNickname())
                .profile_image(member.getProfile_image())
                .build());
    }

     //로그아웃
    @Transactional
    public ResponseMessage<?> logout(UserDetailsImpl userDetails) {
        if(jwtUtil.deleteToken(userDetails.getMember().getEmail())){
            return ResponseMessage.success("logout success");
        }

        return ResponseMessage.fail(TOKEN_NOT_FOUND);
    }



    //로그인할 때 생성된 리프레쉬 토큰을 레포지토리에 저장 - 레포지토리에서 찾아서 없애기


    //회원정보수정
    //회원탈퇴
    //회원정보요청


    //메서드 추가
    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

}
