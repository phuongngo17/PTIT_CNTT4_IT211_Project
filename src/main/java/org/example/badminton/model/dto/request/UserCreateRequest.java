package org.example.badminton.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCreateRequest {
    @NotBlank(message = "Họ tên không được để trống")
    private String full_name;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone_number;

    @NotBlank(message = "Vai trò không được để trống")
    private String role;
}
