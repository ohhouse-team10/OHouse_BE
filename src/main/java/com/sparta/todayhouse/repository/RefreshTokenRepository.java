package com.sparta.todayhouse.repository;

import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.RefreshToken;
import io.jsonwebtoken.Jwts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository< RefreshToken, Long> {
    Optional<RefreshToken> findByEmail(String email);

}
