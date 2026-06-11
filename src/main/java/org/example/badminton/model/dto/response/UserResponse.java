package org.example.badminton.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.badminton.model.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String full_name;
    private String email;
    private String phone_number;
    private String role;
    private Boolean is_enable;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .full_name(user.getFull_name())
                .email(user.getEmail())
                .phone_number(user.getPhone_number())
                .role(user.getRole())
                .is_enable(user.getIs_enable())
                .build();
    }
}
