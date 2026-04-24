package com.booknest.booknest_backend.config;

import com.booknest.booknest_backend.model.User;
import com.booknest.booknest_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@booknest.com";
        
        // Check if admin already exists so we don't duplicate it on every restart
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail(adminEmail);
            // Feel free to change this default password!
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRole(User.Role.ADMIN);
            
            userRepository.save(admin);
            System.out.println("✅ Default Admin user created successfully!");
        } else {
            System.out.println("ℹ️ Default Admin user already exists.");
        }
    }
}
