package com.sparta.todayhouse.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.MemberRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.LoginResponseDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.jwt.JwtUtil;
import com.sparta.todayhouse.jwt.TokenDto;
import com.sparta.todayhouse.repository.MemberRepository;
import com.sparta.todayhouse.shared.Role;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.todayhouse.shared.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;      //
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ResponseMessage<?> isPresentMember(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(null == member) return ResponseMessage.fail(MEMBER_NOT_FOUND);

        return ResponseMessage.success(member);
    }

    //회원가입
    @Transactional
    public ResponseMessage<?> signup(SignupRequestDto requestDto){
        ResponseMessage<?> data = isPresentMember(requestDto.getEmail());
        if(data.getIsSuccess()) return ResponseMessage.fail(DUPLICATE_EMAIL);

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
        ResponseMessage<?> data = isPresentMember(requestDto.getEmail());

        if(!data.getIsSuccess()) return ResponseMessage.fail(MEMBER_NOT_FOUND);

        Member member = (Member)data.getData();
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
        jwtUtil.deleteToken(userDetails.getMember().getEmail());

        return ResponseMessage.success("logout success");
    }


    //회원정보수정
    @Transactional
    public ResponseMessage<?> updateMember(MemberRequestDto requestDto, UserDetailsImpl userDetails){

        Member member = userDetails.getMember();
        member.updateMember(requestDto);
        memberRepository.save(member);
        return ResponseMessage.success("edit success");
    }

    //회원탈퇴 로그인되어있는 id를 repository에서 delete
    @Transactional
    public ResponseMessage<?> deleteMember(String email, UserDetailsImpl userDetails){
        ResponseMessage<?> response = isPresentMember(email);
        if(!response.getIsSuccess()) return response;

        Member member = userDetails.getMember();

        memberRepository.delete(member);
        return ResponseMessage.success("delete success");

//        Optional<Member> member = memberRepository.findById(id);
//        memberRepository.deleteByMember(member);

//        Member member = memberRepository.findById(id);
//        memberRepository.delete(member);
//        return ResponseMessage.success("delete success");
    }

}
