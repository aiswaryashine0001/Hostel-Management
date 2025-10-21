package com.hostel.management.controller;

import com.hostel.management.entity.Admin;
import com.hostel.management.entity.Room;
import com.hostel.management.entity.Student;
import com.hostel.management.repository.AdminRepository;
import com.hostel.management.repository.RoomRepository;
import com.hostel.management.repository.StudentRepository;
import com.hostel.management.service.RoomAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST API Controller for admin operations
 */
@RestController
@RequestMapping("/api")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RoomAllocationService allocationService;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Admin login endpoint
     */
    @PostMapping("/admin_login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            if (username == null || password == null) {
                response.put("success", false);
                response.put("message", "Username and password are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<Admin> adminOpt = adminRepository.findByUsername(username);
            
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                
                if (passwordEncoder.matches(password, admin.getPasswordHash())) {
                    // Set session
                    session.setAttribute("admin_id", admin.getId());
                    session.setAttribute("admin_name", admin.getUsername());
                    
                    response.put("success", true);
                    response.put("message", "Admin login successful");
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("success", false);
            response.put("message", "Invalid admin credentials");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Admin login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all rooms endpoint
     */
    @GetMapping("/rooms")
    public ResponseEntity<Map<String, Object>> getAllRooms() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("rooms", roomRepository.findAll());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch rooms: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Simple test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * Database health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            long studentCount = studentRepository.count();
            long roomCount = roomRepository.count();
            long adminCount = adminRepository.count();
            
            response.put("success", true);
            response.put("database", "connected");
            response.put("students", studentCount);
            response.put("rooms", roomCount);
            response.put("admins", adminCount);
            response.put("message", "Database is working properly");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("database", "error");
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Get all students endpoint (for admin)
     */
    @GetMapping("/students")
    public ResponseEntity<Map<String, Object>> getAllStudents(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("AdminController: /api/students endpoint called");
            
            // Temporarily disable session check for debugging
            // Long adminId = (Long) session.getAttribute("admin_id");
            // if (adminId == null) {
            //     response.put("success", false);
            //     response.put("message", "Admin access required");
            //     return ResponseEntity.badRequest().body(response);
            // }
            
            var students = studentRepository.findAll();
            System.out.println("Found " + students.size() + " registered students in database");
            
            response.put("students", students);
            response.put("success", true);
            response.put("count", students.size());
            response.put("message", "Found " + students.size() + " registered students");
            
            System.out.println("Returning response with " + students.size() + " students");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error in /api/students: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to fetch students: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Create new room endpoint
     */
    @PostMapping("/create_room")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check admin access
            Long adminId = (Long) session.getAttribute("admin_id");
            if (adminId == null) {
                response.put("success", false);
                response.put("message", "Admin access required");
                return ResponseEntity.badRequest().body(response);
            }
            
            String roomNumber = request.get("room_number");
            String building = request.get("building");
            String floorStr = request.get("floor");
            String capacityStr = request.get("capacity");
            String amenities = request.get("amenities");
            
            if (roomNumber == null || building == null) {
                response.put("success", false);
                response.put("message", "Room number and building are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if room already exists
            if (roomRepository.existsByRoomNumber(roomNumber)) {
                response.put("success", false);
                response.put("message", "Room number already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setBuilding(building);
            
            if (floorStr != null && !floorStr.isEmpty()) {
                room.setFloor(Integer.parseInt(floorStr));
            }
            
            if (capacityStr != null && !capacityStr.isEmpty()) {
                room.setCapacity(Integer.parseInt(capacityStr));
            }
            
            room.setAmenities(amenities);
            room.setStatus("available");
            room.setOccupied(0);
            
            roomRepository.save(room);
            
            response.put("success", true);
            response.put("message", "Room created successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create room: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Run room allocation algorithm endpoint
     */
    @PostMapping("/allocate_rooms")
    public ResponseEntity<Map<String, Object>> allocateRooms(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check admin access
            Long adminId = (Long) session.getAttribute("admin_id");
            if (adminId == null) {
                response.put("success", false);
                response.put("message", "Admin access required");
                return ResponseEntity.badRequest().body(response);
            }
            
            RoomAllocationService.AllocationResult result = allocationService.allocateRooms();
            
            response.put("success", true);
            response.put("message", result.getMessage());
            response.put("results", Map.of(
                "allocated_count", result.getAllocatedCount(),
                "total_students", result.getTotalStudents(),
                "details", result.getDetails()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Allocation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}