package org.example.badminton.service;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.entity.TokenBlacklist;
import org.example.badminton.repository.TokenBlacklistRepository;
import org.example.badminton.security.jwt.JWTProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JWTProvider jwtProvider;

    public void blacklistToken(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        if (tokenBlacklistRepository.existsByToken(token)) {
            return;
        }
        LocalDateTime expiredAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(jwtProvider.getExpirationFromToken(token)),
                ZoneId.systemDefault()
        );
        tokenBlacklistRepository.save(TokenBlacklist.builder()
                .token(token)
                .expired_at(expiredAt)
                .build());
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
