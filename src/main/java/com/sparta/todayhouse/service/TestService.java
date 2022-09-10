package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.request.MemberRequestDto;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.MemberRepository;
import com.sparta.todayhouse.shared.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Member createAdmin(MemberRequestDto requestDto) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null != member) return member;

        member = memberRepository.save(Member.builder()
                .email(requestDto.getEmail())
                .role(Role.ADMIN)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .profile_image("")
                .status_message("")
                .build());

        return member;
    }
}
