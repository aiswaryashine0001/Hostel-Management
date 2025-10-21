package com.hostel.management.service;

import com.hostel.management.entity.Student;
import com.hostel.management.entity.StudentPreferences;
import com.hostel.management.entity.Room;
import com.hostel.management.entity.RoomAllocation;
import com.hostel.management.repository.StudentRepository;
import com.hostel.management.repository.RoomRepository;
import com.hostel.management.repository.RoomAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for room allocation algorithm with compatibility scoring
 */
@Service
public class RoomAllocationService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private RoomAllocationRepository roomAllocationRepository;
    
    // Compatibility weights for different preferences
    private static final Map<String, Double> PREFERENCE_WEIGHTS = Map.of(
        "sleepTime", 0.15,
        "wakeTime", 0.15,
        "studyPreference", 0.12,
        "noiseTolerance", 0.12,
        "cleanlinessLevel", 0.10,
        "socialPreference", 0.10,
        "musicPreference", 0.08,
        "visitorFrequency", 0.08,
        "temperaturePreference", 0.05,
        "smokingPreference", 0.15  // High weight for smoking compatibility
    );
    
    private static final double MINIMUM_COMPATIBILITY_SCORE = 60.0;
    
    /**
     * Main room allocation method
     */
    @Transactional
    public AllocationResult allocateRooms() {
        List<Student> unallocatedStudents = studentRepository.findStudentsWithPreferencesButNoAllocation();
        List<Room> availableRooms = roomRepository.findAvailableRooms();
        
        if (unallocatedStudents.isEmpty()) {
            return new AllocationResult(0, 0, new ArrayList<>(), "No students to allocate");
        }
        
        if (availableRooms.isEmpty()) {
            return new AllocationResult(0, unallocatedStudents.size(), new ArrayList<>(), "No available rooms");
        }
        
        int allocatedCount = 0;
        List<AllocationDetail> allocationDetails = new ArrayList<>();
        
        // Sort students by registration date (first come, first serve as tiebreaker)
        unallocatedStudents.sort(Comparator.comparing(Student::getCreatedAt));
        
        for (Student student : unallocatedStudents) {
            RoomCompatibility bestMatch = findBestRoom(student, availableRooms);
            
            if (bestMatch != null && bestMatch.getCompatibilityScore() >= MINIMUM_COMPATIBILITY_SCORE) {
                Room room = bestMatch.getRoom();
                
                // Create allocation
                RoomAllocation allocation = new RoomAllocation(student, room, bestMatch.getCompatibilityScore());
                roomAllocationRepository.save(allocation);
                
                // Update room occupancy
                room.setOccupied(room.getOccupied() + 1);
                roomRepository.save(room);
                
                // Add to details
                allocationDetails.add(new AllocationDetail(
                    student.getName(),
                    student.getStudentId(),
                    room.getRoomNumber(),
                    bestMatch.getCompatibilityScore()
                ));
                
                allocatedCount++;
                
                // Remove room from available list if it's full
                if (room.getOccupied() >= room.getCapacity()) {
                    availableRooms.remove(room);
                }
            }
        }
        
        String message = String.format("Successfully allocated %d out of %d students", 
                                     allocatedCount, unallocatedStudents.size());
        
        return new AllocationResult(allocatedCount, unallocatedStudents.size(), allocationDetails, message);
    }
    
    /**
     * Find the best room for a student based on compatibility
     */
    private RoomCompatibility findBestRoom(Student student, List<Room> availableRooms) {
        RoomCompatibility bestMatch = null;
        double bestScore = -1.0;
        
        for (Room room : availableRooms) {
            if (!room.isAvailable()) {
                continue;
            }
            
            double roomScore = calculateRoomCompatibility(student, room);
            
            if (roomScore > bestScore) {
                bestScore = roomScore;
                bestMatch = new RoomCompatibility(room, roomScore);
            }
        }
        
        return bestMatch;
    }
    
    /**
     * Calculate compatibility score for a student with a specific room
     */
    private double calculateRoomCompatibility(Student student, Room room) {
        List<RoomAllocation> currentAllocations = roomAllocationRepository.findByRoomAndStatus(room, "active");
        
        if (currentAllocations.isEmpty()) {
            // Empty room - good default score
            return 75.0;
        }
        
        // Calculate average compatibility with existing roommates
        List<Double> compatibilityScores = new ArrayList<>();
        
        for (RoomAllocation allocation : currentAllocations) {
            Student roommate = allocation.getStudent();
            if (roommate.getPreferences() != null) {
                double score = calculateCompatibilityScore(student.getPreferences(), roommate.getPreferences());
                compatibilityScores.add(score);
            }
        }
        
        if (compatibilityScores.isEmpty()) {
            return 50.0; // Default score if no preferences available
        }
        
        return compatibilityScores.stream().mapToDouble(Double::doubleValue).average().orElse(50.0);
    }
    
    /**
     * Calculate compatibility score between two students' preferences
     */
    public double calculateCompatibilityScore(StudentPreferences prefs1, StudentPreferences prefs2) {
        if (prefs1 == null || prefs2 == null) {
            return 50.0; // Default score
        }
        
        double totalScore = 0.0;
        double totalWeight = 0.0;
        
        // Sleep time compatibility
        if (prefs1.getSleepTime() != null && prefs2.getSleepTime() != null) {
            double score = calculateTimeCompatibility(prefs1.getSleepTime(), prefs2.getSleepTime());
            totalScore += score * PREFERENCE_WEIGHTS.get("sleepTime");
            totalWeight += PREFERENCE_WEIGHTS.get("sleepTime");
        }
        
        // Wake time compatibility
        if (prefs1.getWakeTime() != null && prefs2.getWakeTime() != null) {
            double score = calculateTimeCompatibility(prefs1.getWakeTime(), prefs2.getWakeTime());
            totalScore += score * PREFERENCE_WEIGHTS.get("wakeTime");
            totalWeight += PREFERENCE_WEIGHTS.get("wakeTime");
        }
        
        // Study preference compatibility
        if (prefs1.getStudyPreference() != null && prefs2.getStudyPreference() != null) {
            double score = calculateCategoricalCompatibility(prefs1.getStudyPreference(), prefs2.getStudyPreference());
            totalScore += score * PREFERENCE_WEIGHTS.get("studyPreference");
            totalWeight += PREFERENCE_WEIGHTS.get("studyPreference");
        }
        
        // Noise tolerance compatibility
        if (prefs1.getNoiseTolerance() != null && prefs2.getNoiseTolerance() != null) {
            double score = calculateOrdinalCompatibility(prefs1.getNoiseTolerance(), prefs2.getNoiseTolerance());
            totalScore += score * PREFERENCE_WEIGHTS.get("noiseTolerance");
            totalWeight += PREFERENCE_WEIGHTS.get("noiseTolerance");
        }
        
        // Cleanliness level compatibility
        if (prefs1.getCleanlinessLevel() != null && prefs2.getCleanlinessLevel() != null) {
            double score = calculateOrdinalCompatibility(prefs1.getCleanlinessLevel(), prefs2.getCleanlinessLevel());
            totalScore += score * PREFERENCE_WEIGHTS.get("cleanlinessLevel");
            totalWeight += PREFERENCE_WEIGHTS.get("cleanlinessLevel");
        }
        
        // Social preference compatibility
        if (prefs1.getSocialPreference() != null && prefs2.getSocialPreference() != null) {
            double score = calculateSocialCompatibility(prefs1.getSocialPreference(), prefs2.getSocialPreference());
            totalScore += score * PREFERENCE_WEIGHTS.get("socialPreference");
            totalWeight += PREFERENCE_WEIGHTS.get("socialPreference");
        }
        
        // Music preference compatibility
        if (prefs1.getMusicPreference() != null && prefs2.getMusicPreference() != null) {
            double score = calculateCategoricalCompatibility(prefs1.getMusicPreference(), prefs2.getMusicPreference());
            totalScore += score * PREFERENCE_WEIGHTS.get("musicPreference");
            totalWeight += PREFERENCE_WEIGHTS.get("musicPreference");
        }
        
        // Visitor frequency compatibility
        if (prefs1.getVisitorFrequency() != null && prefs2.getVisitorFrequency() != null) {
            double score = calculateCategoricalCompatibility(prefs1.getVisitorFrequency(), prefs2.getVisitorFrequency());
            totalScore += score * PREFERENCE_WEIGHTS.get("visitorFrequency");
            totalWeight += PREFERENCE_WEIGHTS.get("visitorFrequency");
        }
        
        // Temperature preference compatibility
        if (prefs1.getTemperaturePreference() != null && prefs2.getTemperaturePreference() != null) {
            double score = calculateCategoricalCompatibility(prefs1.getTemperaturePreference(), prefs2.getTemperaturePreference());
            totalScore += score * PREFERENCE_WEIGHTS.get("temperaturePreference");
            totalWeight += PREFERENCE_WEIGHTS.get("temperaturePreference");
        }
        
        // Smoking preference compatibility (high importance)
        if (prefs1.getSmokingPreference() != null && prefs2.getSmokingPreference() != null) {
            double score = prefs1.getSmokingPreference().equalsIgnoreCase(prefs2.getSmokingPreference()) ? 1.0 : 0.0;
            totalScore += score * PREFERENCE_WEIGHTS.get("smokingPreference");
            totalWeight += PREFERENCE_WEIGHTS.get("smokingPreference");
        }
        
        // Interests compatibility bonus
        if (prefs1.getInterests() != null && prefs2.getInterests() != null) {
            double interestsScore = calculateInterestsCompatibility(prefs1.getInterests(), prefs2.getInterests());
            totalScore += interestsScore * 0.1;
            totalWeight += 0.1;
        }
        
        return totalWeight > 0 ? (totalScore / totalWeight) * 100 : 50.0;
    }
    
    /**
     * Calculate time-based compatibility
     */
    private double calculateTimeCompatibility(String time1, String time2) {
        try {
            double hour1 = timeToHour(time1);
            double hour2 = timeToHour(time2);
            
            double diff = Math.abs(hour1 - hour2);
            
            // Handle wrap-around (e.g., 23:00 and 01:00)
            if (diff > 12) {
                diff = 24 - diff;
            }
            
            // Score decreases as time difference increases
            return Math.max(0.0, 1.0 - (diff / 3.0));
        } catch (Exception e) {
            return 0.5; // Default score if parsing fails
        }
    }
    
    /**
     * Convert time string to hour value
     */
    private double timeToHour(String timeStr) {
        if (timeStr == null) return 12.0;
        
        String time = timeStr.toLowerCase().trim();
        
        Map<String, Double> timeMappings = Map.of(
            "early morning", 6.0,
            "morning", 8.0,
            "late morning", 10.0,
            "noon", 12.0,
            "afternoon", 14.0,
            "evening", 18.0,
            "late evening", 20.0,
            "night", 22.0,
            "late night", 24.0,
            "midnight", 0.0
        );
        
        return timeMappings.getOrDefault(time, 12.0);
    }
    
    /**
     * Calculate ordinal compatibility (low/medium/high)
     */
    private double calculateOrdinalCompatibility(String value1, String value2) {
        Map<String, Integer> ordinalMap = Map.of(
            "low", 1,
            "medium", 2,
            "high", 3
        );
        
        int val1 = ordinalMap.getOrDefault(value1.toLowerCase(), 2);
        int val2 = ordinalMap.getOrDefault(value2.toLowerCase(), 2);
        
        int diff = Math.abs(val1 - val2);
        
        return Math.max(0.0, 1.0 - (diff / 2.0));
    }
    
    /**
     * Calculate categorical compatibility
     */
    private double calculateCategoricalCompatibility(String value1, String value2) {
        return value1.equalsIgnoreCase(value2) ? 1.0 : 0.5;
    }
    
    /**
     * Calculate social compatibility with specific rules
     */
    private double calculateSocialCompatibility(String social1, String social2) {
        Map<String, Map<String, Double>> socialMatrix = Map.of(
            "extrovert", Map.of(
                "extrovert", 1.0,
                "ambivert", 0.8,
                "introvert", 0.4
            ),
            "introvert", Map.of(
                "introvert", 1.0,
                "ambivert", 0.8,
                "extrovert", 0.4
            ),
            "ambivert", Map.of(
                "ambivert", 1.0,
                "extrovert", 0.8,
                "introvert", 0.8
            )
        );
        
        return socialMatrix.getOrDefault(social1.toLowerCase(), Map.of())
                          .getOrDefault(social2.toLowerCase(), 0.5);
    }
    
    /**
     * Calculate interests compatibility using Jaccard similarity
     */
    private double calculateInterestsCompatibility(String interests1, String interests2) {
        if (interests1 == null || interests2 == null || interests1.trim().isEmpty() || interests2.trim().isEmpty()) {
            return 0.5;
        }
        
        Set<String> set1 = Arrays.stream(interests1.split("[,;]"))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toSet());
        
        Set<String> set2 = Arrays.stream(interests2.split("[,;]"))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toSet());
        
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.5;
        }
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    // Helper classes
    public static class AllocationResult {
        private int allocatedCount;
        private int totalStudents;
        private List<AllocationDetail> details;
        private String message;
        
        public AllocationResult(int allocatedCount, int totalStudents, List<AllocationDetail> details, String message) {
            this.allocatedCount = allocatedCount;
            this.totalStudents = totalStudents;
            this.details = details;
            this.message = message;
        }
        
        // Getters
        public int getAllocatedCount() { return allocatedCount; }
        public int getTotalStudents() { return totalStudents; }
        public List<AllocationDetail> getDetails() { return details; }
        public String getMessage() { return message; }
    }
    
    public static class AllocationDetail {
        private String studentName;
        private String studentId;
        private String roomNumber;
        private double compatibilityScore;
        
        public AllocationDetail(String studentName, String studentId, String roomNumber, double compatibilityScore) {
            this.studentName = studentName;
            this.studentId = studentId;
            this.roomNumber = roomNumber;
            this.compatibilityScore = Math.round(compatibilityScore * 100.0) / 100.0; // Round to 2 decimals
        }
        
        // Getters
        public String getStudentName() { return studentName; }
        public String getStudentId() { return studentId; }
        public String getRoomNumber() { return roomNumber; }
        public double getCompatibilityScore() { return compatibilityScore; }
    }
    
    private static class RoomCompatibility {
        private Room room;
        private double compatibilityScore;
        
        public RoomCompatibility(Room room, double compatibilityScore) {
            this.room = room;
            this.compatibilityScore = compatibilityScore;
        }
        
        public Room getRoom() { return room; }
        public double getCompatibilityScore() { return compatibilityScore; }
    }
}