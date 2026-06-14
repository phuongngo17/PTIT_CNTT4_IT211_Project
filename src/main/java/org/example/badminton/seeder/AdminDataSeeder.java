package org.example.badminton.seeder;

import lombok.RequiredArgsConstructor;
import org.example.badminton.model.constants.RoleName;
import org.example.badminton.model.entity.BadmintonCluster;
import org.example.badminton.model.entity.Court;
import org.example.badminton.model.entity.Timeslot;
import org.example.badminton.model.entity.User;
import org.example.badminton.repository.BadmintonClusterRepository;
import org.example.badminton.repository.CourtRepository;
import org.example.badminton.repository.TimeslotRepository;
import org.example.badminton.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class AdminDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BadmintonClusterRepository clusterRepository;
    private final CourtRepository courtRepository;
    private final TimeslotRepository timeslotRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCourtsAndTimeslots();
    }

    private void seedAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .full_name("Quản trị viên")
                    .email("admin@badminton.com")
                    .phone_number("0939284787")
                    .role(RoleName.ADMIN.name())
                    .is_enable(true)
                    .build();
            userRepository.save(user);
        }
    }

    private void seedCourtsAndTimeslots() {
        if (timeslotRepository.count() == 0) {
            timeslotRepository.save(Timeslot.builder()
                    .start_time(LocalTime.of(6, 0))
                    .end_time(LocalTime.of(8, 0))
                    .label("06:00 - 08:00")
                    .build());
            timeslotRepository.save(Timeslot.builder()
                    .start_time(LocalTime.of(8, 0))
                    .end_time(LocalTime.of(10, 0))
                    .label("08:00 - 10:00")
                    .build());
            timeslotRepository.save(Timeslot.builder()
                    .start_time(LocalTime.of(18, 0))
                    .end_time(LocalTime.of(20, 0))
                    .label("18:00 - 20:00")
                    .build());
        }

        if (courtRepository.count() == 0) {
            BadmintonCluster cluster = clusterRepository.save(BadmintonCluster.builder()
                    .name("Cụm sân Thể Thao ABC")
                    .address("123 Đường Nguyễn Trãi, Hà Nội")
                    .hot_line("19001234")
                    .build());

            courtRepository.save(Court.builder()
                    .name("Sân số 1")
                    .price(new BigDecimal("150000"))
                    .is_active(true)
                    .cluster(cluster)
                    .build());
            courtRepository.save(Court.builder()
                    .name("Sân số 2")
                    .price(new BigDecimal("180000"))
                    .is_active(true)
                    .cluster(cluster)
                    .build());
        }
    }
}
