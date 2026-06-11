package org.example.badminton.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.badminton.model.dto.request.UserCreateRequest;
import org.example.badminton.model.dto.request.UserUpdateRequest;
import org.example.badminton.model.dto.response.ApiDataResponse;
import org.example.badminton.model.dto.response.UserResponse;
import org.example.badminton.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @Value("${page_user_size}")
    private Integer pageSize;

    @PostMapping
    public ResponseEntity<ApiDataResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiDataResponse<>(
                true,
                "Tạo người dùng thành công",
                userService.createUser(request),
                HttpStatus.CREATED
        ));
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<UserResponse>>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Lấy danh sách người dùng thành công",
                userService.getUsers(keyword, role, page - 1, pageSize),
                HttpStatus.OK
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Lấy thông tin người dùng thành công",
                userService.getUserById(id),
                HttpStatus.OK
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Cập nhật người dùng thành công",
                userService.updateUser(id, request),
                HttpStatus.OK
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiDataResponse<>(
                true,
                "Xóa người dùng thành công",
                null,
                HttpStatus.OK
        ));
    }
}
