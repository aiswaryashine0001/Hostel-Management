package com.hostel.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application class for Hostel Management System
 * 
 * This application provides:
 * - Student registration and authentication
 * - Smart roommate matching based on preferences
 * - Room allocation algorithm
 * - Admin dashboard for system management
 */
@SpringBootApplication
@EnableJpaRepositories
public class HostelManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostelManagementApplication.class, args);
    }
}