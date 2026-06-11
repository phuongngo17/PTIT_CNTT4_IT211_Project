package org.example.badminton.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest {
    @NotBlank(message = "Họ tên không được để trống")
    private String full_name;

    @NotBlank(message = "Username không được để trống")
    private String username;

    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone_number;

    @NotBlank(message = "Vai trò không được để trống")
    private String role;

    private Boolean is_enable;
}
