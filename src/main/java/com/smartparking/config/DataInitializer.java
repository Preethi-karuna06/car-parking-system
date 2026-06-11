package com.smartparking.config;

import com.smartparking.entity.User;
import com.smartparking.enums.Role;
import com.smartparking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@smartparking.com")) {
            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@smartparking.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("9000000000");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            log.info("✅ Admin created: admin@smartparking.com / admin123");
        }
        log.info("🚗 Smart Parking System running at http://localhost:8080");
    }
}
