package org.example.badminton.security.principal;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        if (!Boolean.TRUE.equals(user.getIs_enable())) {
            throw new UsernameNotFoundException("Tài khoản đã bị khóa");
        }

        return CustomUserDetails.fromUser(user, List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        ));
    }
}
