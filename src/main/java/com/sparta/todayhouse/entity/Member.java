package com.sparta.todayhouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String profile_image;

    @Column(nullable = false)
    String status_message;

    public boolean validatePassword(PasswordEncoder passswordEncoder, String password){
        return passswordEncoder.matches(password, this.password);
    }
}
