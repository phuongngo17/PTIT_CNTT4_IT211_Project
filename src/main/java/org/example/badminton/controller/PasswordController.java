package org.example.badminton.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.badminton.model.dto.request.ChangePasswordRequest;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final AuthService authService;

    @PutMapping("/change")
    public ResponseEntity<ApiDataResponse<String>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(authentication.getName(), request);

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