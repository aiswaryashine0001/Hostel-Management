package com.hostel.management.controller;

import com.hostel.management.dto.StudentDto;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * Debug allocation readiness
     */
    @GetMapping("/allocation_debug")
    public ResponseEntity<Map<String, Object>> allocationDebug() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check students with preferences but no allocation
            List<Student> studentsWithPrefs = studentRepository.findStudentsWithPreferencesButNoAllocation();
            List<Room> availableRooms = roomRepository.findAvailableRooms();
            List<Student> allStudents = studentRepository.findAll();
            List<Room> allRooms = roomRepository.findAll();
            
            response.put("total_students", allStudents.size());
            response.put("students_with_preferences_no_allocation", studentsWithPrefs.size());
            response.put("total_rooms", allRooms.size());
            response.put("available_rooms", availableRooms.size());
            
            // List students with preferences
            List<Map<String, Object>> studentDetails = new ArrayList<>();
            for (Student s : allStudents) {
                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("student_id", s.getStudentId());
                studentInfo.put("name", s.getName());
                studentInfo.put("has_preferences", s.getPreferences() != null);
                studentInfo.put("has_allocation", s.getRoomAllocation() != null);
                studentDetails.add(studentInfo);
            }
            response.put("student_details", studentDetails);
            
            // List room details
            List<Map<String, Object>> roomDetails = new ArrayList<>();
            for (Room r : allRooms) {
                Map<String, Object> roomInfo = new HashMap<>();
                roomInfo.put("room_number", r.getRoomNumber());
                roomInfo.put("capacity", r.getCapacity());
                roomInfo.put("occupied", r.getOccupied());
                roomInfo.put("status", r.getStatus());
                roomInfo.put("is_available", r.getOccupied() < r.getCapacity() && "available".equals(r.getStatus()));
                roomDetails.add(roomInfo);
            }
            response.put("room_details", roomDetails);
            
            response.put("success", true);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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
            
            List<Student> students = studentRepository.findAll();
            System.out.println("Found " + students.size() + " registered students in database");
            
            // Convert to DTOs to avoid circular reference issues
            List<StudentDto> studentDtos = students.stream()
                .map(StudentDto::new)
                .collect(Collectors.toList());
            
            response.put("students", studentDtos);
            response.put("success", true);
            response.put("count", studentDtos.size());
            response.put("message", "Found " + studentDtos.size() + " registered students");
            
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
            // Get students with preferences but no allocation
            List<Student> allStudents = studentRepository.findAll();
            List<Student> studentsToAllocate = new ArrayList<>();
            
            for (Student student : allStudents) {
                if (student.getPreferences() != null && student.getRoomAllocation() == null) {
                    studentsToAllocate.add(student);
                }
            }
            
            // Get or create rooms
            List<Room> availableRooms = roomRepository.findAll();
            if (availableRooms.isEmpty()) {
                // Create a default room if none exist
                Room defaultRoom = new Room();
                defaultRoom.setRoomNumber("R001");
                defaultRoom.setBuilding("A");
                defaultRoom.setFloor(1);
                defaultRoom.setCapacity(4);
                defaultRoom.setOccupied(0);
                defaultRoom.setStatus("available");
                roomRepository.save(defaultRoom);
                availableRooms.add(defaultRoom);
            }
            
            int allocatedCount = 0;
            List<Map<String, Object>> allocationDetails = new ArrayList<>();
            
            for (Student student : studentsToAllocate) {
                // Find first available room
                Room targetRoom = null;
                for (Room room : availableRooms) {
                    if (room.getOccupied() < room.getCapacity()) {
                        targetRoom = room;
                        break;
                    }
                }
                
                if (targetRoom != null) {
                    // Create allocation
                    RoomAllocation allocation = new RoomAllocation();
                    allocation.setStudent(student);
                    allocation.setRoom(targetRoom);
                    allocation.setCompatibilityScore(75.0); // Default score
                    allocation.setStatus("active");
                    // allocationDate is auto-set by @CreationTimestamp
                    roomAllocationRepository.save(allocation);
                    
                    // Update room occupancy
                    targetRoom.setOccupied(targetRoom.getOccupied() + 1);
                    roomRepository.save(targetRoom);
                    
                    // Update student
                    student.setRoomAllocation(allocation);
                    studentRepository.save(student);
                    
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("studentName", student.getName());
                    detail.put("studentId", student.getStudentId());
                    detail.put("roomNumber", targetRoom.getRoomNumber());
                    detail.put("compatibilityScore", 75.0);
                    allocationDetails.add(detail);
                    
                    allocatedCount++;
                }
            }
            
            response.put("success", true);
            response.put("message", "Allocated " + allocatedCount + " out of " + studentsToAllocate.size() + " students");
            response.put("results", Map.of(
                "allocated_count", allocatedCount,
                "total_students", studentsToAllocate.size(),
                "details", allocationDetails
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Allocation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}