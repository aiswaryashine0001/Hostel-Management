package com.hostel.management.config;

import com.hostel.management.entity.Admin;
import com.hostel.management.entity.Room;
import com.hostel.management.entity.Student;
import com.hostel.management.repository.AdminRepository;
import com.hostel.management.repository.RoomRepository;
import com.hostel.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialize sample data for the hostel management system
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize admin user if not exists
        if (!adminRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@hostel.edu");
            adminRepository.save(admin);
            System.out.println("Default admin created - Username: admin, Password: admin123");
        }
        
        // Initialize sample rooms if not exists
        if (roomRepository.count() == 0) {
            createSampleRooms();
            System.out.println("Sample rooms created successfully");
        }
        
        // Show count of registered students
        long studentCount = studentRepository.count();
        System.out.println("Current registered students in database: " + studentCount);
    }
    
    private void createSampleRooms() {
        String[][] roomData = {
            {"A101", "2", "1", "Block A", "WiFi, AC, Study Table"},
            {"A102", "2", "1", "Block A", "WiFi, AC, Study Table"},
            {"A103", "2", "1", "Block A", "WiFi, AC, Study Table"},
            {"A201", "2", "2", "Block A", "WiFi, AC, Study Table"},
            {"A202", "2", "2", "Block A", "WiFi, AC, Study Table"},
            {"B101", "3", "1", "Block B", "WiFi, Fan, Study Table"},
            {"B102", "3", "1", "Block B", "WiFi, Fan, Study Table"},
            {"B201", "3", "2", "Block B", "WiFi, Fan, Study Table"},
            {"C101", "4", "1", "Block C", "WiFi, AC, Study Table, Balcony"},
            {"C102", "4", "1", "Block C", "WiFi, AC, Study Table, Balcony"}
        };
        
        for (String[] data : roomData) {
            Room room = new Room();
            room.setRoomNumber(data[0]);
            room.setCapacity(Integer.parseInt(data[1]));
            room.setFloor(Integer.parseInt(data[2]));
            room.setBuilding(data[3]);
            room.setAmenities(data[4]);
            room.setStatus("available");
            room.setOccupied(0);
            roomRepository.save(room);
        }
    }
}