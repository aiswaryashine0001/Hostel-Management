package com.hostel.management.controller;

import com.hostel.management.entity.Student;
import com.hostel.management.entity.StudentPreferences;
import com.hostel.management.repository.StudentRepository;
import com.hostel.management.repository.StudentPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST API Controller for student operations
 */
@RestController
@RequestMapping("/api")
public class StudentController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private StudentPreferencesRepository preferencesRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Student registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String studentId = request.get("student_id");
            String name = request.get("name");
            String email = request.get("email");
            String phone = request.get("phone");
            String password = request.get("password");
            String course = request.get("course");
            String yearStr = request.get("year");
            String gender = request.get("gender");
            
            // Validation
            if (studentId == null || name == null || email == null || password == null) {
                response.put("success", false);
                response.put("message", "Required fields are missing");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if student already exists
            if (studentRepository.existsByStudentId(studentId)) {
                response.put("success", false);
                response.put("message", "Student ID already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (studentRepository.existsByEmail(email)) {
                response.put("success", false);
                response.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create new student
            Student student = new Student();
            student.setStudentId(studentId);
            student.setName(name);
            student.setEmail(email);
            student.setPhone(phone);
            student.setPasswordHash(passwordEncoder.encode(password));
            student.setCourse(course);
            if (yearStr != null && !yearStr.isEmpty()) {
                student.setYear(Integer.parseInt(yearStr));
            }
            student.setGender(gender);
            
            studentRepository.save(student);
            
            response.put("success", true);
            response.put("message", "Registration successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Student login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String studentId = request.get("student_id");
            String password = request.get("password");
            
            if (studentId == null || password == null) {
                response.put("success", false);
                response.put("message", "Student ID and password are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
            
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                
                if (passwordEncoder.matches(password, student.getPasswordHash())) {
                    // Set session
                    session.setAttribute("student_id", student.getId());
                    session.setAttribute("student_name", student.getName());
                    
                    response.put("success", true);
                    response.put("message", "Login successful");
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Save student preferences endpoint
     */
    @PostMapping("/preferences")
    public ResponseEntity<Map<String, Object>> savePreferences(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long studentId = (Long) session.getAttribute("student_id");
            if (studentId == null) {
                response.put("success", false);
                response.put("message", "Not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<Student> studentOpt = studentRepository.findById(studentId);
            if (!studentOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Student not found");
                return ResponseEntity.badRequest().body(response);
            }
            
            Student student = studentOpt.get();
            
            // Check if preferences already exist
            Optional<StudentPreferences> existingPrefs = preferencesRepository.findByStudent(student);
            StudentPreferences preferences;
            
            if (existingPrefs.isPresent()) {
                preferences = existingPrefs.get();
            } else {
                preferences = new StudentPreferences();
                preferences.setStudent(student);
            }
            
            // Set preference values
            preferences.setSleepTime(request.get("sleep_time"));
            preferences.setWakeTime(request.get("wake_time"));
            preferences.setStudyPreference(request.get("study_preference"));
            preferences.setNoiseTolerance(request.get("noise_tolerance"));
            preferences.setCleanlinessLevel(request.get("cleanliness_level"));
            preferences.setSocialPreference(request.get("social_preference"));
            preferences.setMusicPreference(request.get("music_preference"));
            preferences.setVisitorFrequency(request.get("visitor_frequency"));
            preferences.setTemperaturePreference(request.get("temperature_preference"));
            preferences.setDietaryPreferences(request.get("dietary_preferences"));
            preferences.setInterests(request.get("interests"));
            preferences.setAdditionalNotes(request.get("additional_notes"));
            
            // Save preferences and ensure bidirectional link is set on the student
            StudentPreferences saved = preferencesRepository.save(preferences);
            student.setPreferences(saved);
            studentRepository.save(student);
            
            response.put("success", true);
            response.put("message", "Preferences saved successfully");
            response.put("preferences_id", saved.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save preferences: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    
    /**
     * Logout endpoint (POST)
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        session.invalidate();
        
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Logout endpoint (GET) - for direct navigation
     */
    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}