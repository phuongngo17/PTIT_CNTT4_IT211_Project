package org.example.badminton.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.model.dto.request.*;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.model.dto.response.JWTResponse;
import org.example.badminton.model.dto.response.UserResponse;
import org.example.badminton.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiDataResponse<>(
                true,
                "Đăng ký tài khoản thành công",
                authService.register(request),
                HttpStatus.CREATED
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JWTResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Đăng nhập thành công",
                authService.login(request),
                HttpStatus.OK
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse<JWTResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Cấp lại access token thành công",
                authService.refreshToken(request.getRefreshToken()),
                HttpStatus.OK
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse<String>> logout(
            HttpServletRequest request) {

        String token = extractToken(request);

        authService.logout(token);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đăng xuất thành công",
                        null,
                        HttpStatus.OK
                )
        );
    }

    private String extractToken(HttpServletRequest request) {

        String authorization =
                request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            throw new HttpBadRequestException(
                    "Không tìm thấy Authorization Header"
            );
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new HttpBadRequestException(
                    "Token không đúng định dạng Bearer"
            );
        }

        return authorization.substring(7);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("OK");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đổi mật khẩu thành công",
                        null,
                        HttpStatus.OK
                )
        );
    }
}
