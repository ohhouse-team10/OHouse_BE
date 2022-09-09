package com.sparta.todayhouse.shared;

import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.shared.Authority;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class UserDetailsImpl implements UserDetails{

    private Member member;
    //private Map<String, Object> attributes;

    //UserDetails : 기본 로그인 시 사용
    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    //OAuth2User : OAuth2 로그인 시 사용
//    public UserDetailsImpl(Member member, Map<String, Object> attributes) {
//        //PrincipalOauth2UserService 참고
//        this.member = member;
//        this.attributes = attributes;
//    }

    /*
     *  UserDetails 구현
     * 해당 유저의 권한목록 리턴(지금은 그냥 ROLE_MEMBER 반환하게 해놓음)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_MEMBER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    /*
     *  UserDetails 구현
     * 해당 유저의 PK값 반환
     */
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /*
     *  UserDetails 구현
     * 해당 유저의 비밀번호 리턴
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /*
     *  UserDetails 구현
     * 계정 만료 여부
     *  true : 만료안됨
     *  false : 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     *  UserDetails 구현
     * 계정 잠김 여부
     *  true : 잠기지 않음
     *  false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     *  UserDetails 구현
     * 계정 비밀번호 만료 여부
     *  true : 만료 안됨
     *  false : 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
     *  UserDetails 구현
     *  계정 활성화 여부
     *  true : 활성화됨
     *  false : 활성화 안됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

//    /*
//     * OAuth2User 구현
//     */
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//
//    /*
//     * OAuth2User 구현
//     */
//    @Override
//    public String getName() {
//        String sub = attributes.get("sub").toString();
//        return sub;
//    }
}