package org.example.badminton.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private final String type = "Bearer";
    private Set<String> roles;
}
