package com.sparta.todayhouse.shared;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class PublicMethod {

    public static String rebaseTime(LocalDateTime time){
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
