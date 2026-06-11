package org.example.badminton.service;

import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.model.dto.response.JWTResponse;
import org.example.badminton.model.entity.RefreshToken;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.RefreshTokenRepository;
import org.example.badminton.repository.UserRepository;
import org.example.badminton.security.jwt.JWTProvider;
import org.example.badminton.security.principal.CustomUserDetails;
import org.example.badminton.security.principal.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt-refresh-expired}")
    private Long jwtRefreshExpired;

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpBadRequestException("Không tìm thấy người dùng"));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expired_at(LocalDateTime.now().plusSeconds(jwtRefreshExpired / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public JWTResponse refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new HttpBadRequestException("Refresh token không hợp lệ"));

        if (refreshToken.getExpired_at().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new HttpBadRequestException("Refresh token đã hết hạn");
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService
                .loadUserByUsername(refreshToken.getUser().getUsername());

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return JWTResponse.builder()
                .accessToken(jwtProvider.generateToken(userDetails.getUsername()))
                .roles(roles)
                .build();
    }
}