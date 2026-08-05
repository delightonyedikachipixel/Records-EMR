package com.records.emr.config;

import com.records.emr.data.models.Staff;
import com.records.emr.data.models.enums.Role;
import com.records.emr.data.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (staffRepository.count() == 0) {
            Staff admin = Staff.builder()
                    .name("Default Admin")
                    .role(Role.ADMIN)
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("ChangeMe123!"))
                    .build();
            staffRepository.save(admin);
            System.out.println("Seeded default admin -> username: admin | password: ChangeMe123!  (change this immediately)");
        }
    }
}
