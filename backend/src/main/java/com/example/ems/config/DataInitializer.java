package com.example.ems.config;

import com.example.ems.model.*;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.UserRepository;
import com.example.ems.service.PasswordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository users, DepartmentRepository departments, PasswordService passwords) {
        return args -> {
            if (users.count() == 0) {
                User admin = new User(); admin.setFullName("System Administrator"); admin.setEmail("admin@example.com"); admin.setPasswordHash(passwords.hash("Admin@123")); admin.setRole(UserRole.ADMIN); users.save(admin);
            }
            if (departments.count() == 0) {
                for (String name : new String[]{"IT", "HR", "Finance", "Marketing", "Sales", "Operations"}) { Department d = new Department(); d.setName(name); d.setDescription(name + " department"); departments.save(d); }
            }
        };
    }
}
