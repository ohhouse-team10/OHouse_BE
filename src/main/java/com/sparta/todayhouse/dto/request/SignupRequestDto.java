package com.sparta.todayhouse.dto.request;

import com.sparta.todayhouse.shared.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank // Null, ""," " 허용하지 않음
    @Email
    private String email;

    @NotBlank // Null, ""," " 허용하지 않음
    @Size(min = 4, max = 12) // 최소, 최대 길이를 정할 수 있음
    @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-z\\d]*${3,32}")
    private String password;

    private Role role;
}

