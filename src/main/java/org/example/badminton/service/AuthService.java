package org.example.badminton.service;

import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.model.constants.RoleName;
import org.example.badminton.model.dto.request.ChangePasswordRequest;
import org.example.badminton.model.dto.request.LoginRequest;
import org.example.badminton.model.dto.request.RegisterRequest;
import org.example.badminton.model.dto.response.JWTResponse;
import org.example.badminton.model.dto.response.UserResponse;
import org.example.badminton.model.entity.RefreshToken;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.UserRepository;
import org.example.badminton.security.jwt.JWTProvider;
import org.example.badminton.security.principal.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new HttpBadRequestException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new HttpBadRequestException("Email đã tồn tại");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .full_name(request.getFull_name())
                .email(request.getEmail())
                .phone_number(request.getPhone_number())
                .role(RoleName.CUSTOMER.name())
                .is_enable(true)
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public JWTResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtProvider.generateToken(userDetails.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toSet());

            return JWTResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .roles(roles)
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Sai tài khoản hoặc mật khẩu");
        }
    }

    public JWTResponse refreshToken(String refreshToken) {
        return refreshTokenService.refreshAccessToken(refreshToken);
    }

    public void logout(String accessToken) {
        tokenBlacklistService.blacklistToken(accessToken);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpBadRequestException("Không tìm thấy người dùng"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new HttpBadRequestException("Mật khẩu cũ không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {

            throw new HttpBadRequestException(
                    "Mật khẩu xác nhận không khớp"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }
}
