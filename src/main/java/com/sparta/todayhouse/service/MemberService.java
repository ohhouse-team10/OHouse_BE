package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.MemberRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.MemberResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import static com.sparta.todayhouse.shared.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final String default_image = "3d8a0bd9-de30-49ae-bf47-9e1d5f38571f-profile_placeholder.png";
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service imageUploader;

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
                .profile_image(default_image)
                .status_message("")
                .build();

        memberRepository.save(member);
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
        //반환값 수정
        return ResponseMessage.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .nickname(member.getNickname())
                .profile_image(member.getProfile_image())
                .status_message(member.getStatus_message())
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
    public ResponseMessage<?> updateMember(MemberRequestDto requestDto,
                                           MultipartFile multipartFile,
                                           UserDetailsImpl userDetails){
        Member member = userDetails.getMember();

        ResponseMessage<?> member_data = isPresentMember(member.getEmail());
        if(!member_data.getIsSuccess()) return member_data;
        member = (Member) member_data.getData();

//        if(null == multipartFile) member.updateMember(requestDto);
        if(null == multipartFile) return ResponseMessage.fail(FAIL_TO_UPLOAD);
        else {
            ResponseMessage<?> image_data = imageUploader.uploadFile(multipartFile);
            if(!image_data.getIsSuccess()) return image_data;

            String imageUrl = (String) image_data.getData();
            member.updateMember(requestDto, imageUrl);
        }
        return ResponseMessage.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .nickname(member.getNickname())
                .profile_image(member.getProfile_image())
                .status_message(member.getStatus_message())
                .build());



    }

    //회원탈퇴 로그인되어있는 id를 repository에서 delete
    @Transactional
    public ResponseMessage<?> deleteMember(String email, UserDetailsImpl userDetails){
        ResponseMessage<?> response = isPresentMember(email);
        if(!response.getIsSuccess()) return response;

        Member member = userDetails.getMember();

        memberRepository.delete(member);
        return ResponseMessage.success("delete success");
    }

}
