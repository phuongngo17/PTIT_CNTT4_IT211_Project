package org.example.badminton.seeder;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.constants.RoleName;
import org.example.badminton.model.entity.Role;
import org.example.badminton.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoledataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Arrays.stream(RoleName.values())
                    .map(roleName -> Role.builder().roleName(roleName).build())
                    .forEach(roleRepository::save);
        }
    }
}
