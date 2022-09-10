package com.sparta.todayhouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.todayhouse.shared.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profile_image;

    @Column(nullable = false)
    private String status_message;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Post> posts;

    public boolean validatePassword(PasswordEncoder passswordEncoder, String password) {
        return passswordEncoder.matches(password, this.password);
    }
}
