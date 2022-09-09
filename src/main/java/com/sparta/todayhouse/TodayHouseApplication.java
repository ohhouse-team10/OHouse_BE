package com.sparta.todayhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodayHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodayHouseApplication.class, args);
    }

}
