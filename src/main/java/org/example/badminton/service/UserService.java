package org.example.badminton.service;

import lombok.RequiredArgsConstructor;
import org.example.badminton.exception.HttpBadRequestException;
import org.example.badminton.exception.HttpNotFoundException;
import org.example.badminton.model.constants.RoleName;
import org.example.badminton.model.dto.request.UserCreateRequest;
import org.example.badminton.model.dto.request.UserUpdateRequest;
import org.example.badminton.model.dto.response.UserResponse;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        validateRole(request.getRole());
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
                .role(request.getRole().toUpperCase())
                .is_enable(true)
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public Page<UserResponse> getUsers(String keyword, String role, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.searchUsers(keyword, role, pageable)
                .map(UserResponse::fromEntity);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpNotFoundException("Không tìm thấy người dùng với id: " + id));
        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        validateRole(request.getRole());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpNotFoundException("Không tìm thấy người dùng với id: " + id));

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new HttpBadRequestException("Username đã tồn tại");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new HttpBadRequestException("Email đã tồn tại");
        }

        user.setFull_name(request.getFull_name());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone_number(request.getPhone_number());
        user.setRole(request.getRole().toUpperCase());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getIs_enable() != null) {
            user.setIs_enable(request.getIs_enable());
        }

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpNotFoundException("Không tìm thấy người dùng với id: " + id));
        user.setIs_enable(false);
        userRepository.save(user);
    }

    private void validateRole(String role) {
        boolean valid = Arrays.stream(RoleName.values())
                .map(Enum::name)
                .collect(Collectors.toSet())
                .contains(role.toUpperCase());
        if (!valid) {
            throw new HttpBadRequestException("Vai trò không hợp lệ. Chỉ chấp nhận: ADMIN, MANAGER, CUSTOMER");
        }
    }
}
