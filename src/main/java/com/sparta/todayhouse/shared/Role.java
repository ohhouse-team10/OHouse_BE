package com.sparta.todayhouse.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");                   //진경님 질문

    private final String key;
}
