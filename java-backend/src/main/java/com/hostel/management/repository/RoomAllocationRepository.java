package com.hostel.management.repository;

import com.hostel.management.entity.RoomAllocation;
import com.hostel.management.entity.Student;
import com.hostel.management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RoomAllocation entity operations
 */
@Repository
public interface RoomAllocationRepository extends JpaRepository<RoomAllocation, Long> {
    
    /**
     * Find allocation by student
     */
    Optional<RoomAllocation> findByStudent(Student student);
    
    /**
     * Find allocations by room
     */
    List<RoomAllocation> findByRoom(Room room);
    
    /**
     * Find active allocations by room
     */
    List<RoomAllocation> findByRoomAndStatus(Room room, String status);
    
    /**
     * Find allocation by student and status
     */
    Optional<RoomAllocation> findByStudentAndStatus(Student student, String status);
    
    /**
     * Find all active allocations
     */
    List<RoomAllocation> findByStatus(String status);
    
    /**
     * Check if student has allocation
     */
    boolean existsByStudent(Student student);
    
    /**
     * Check if student has active allocation
     */
    boolean existsByStudentAndStatus(Student student, String status);
    
    /**
     * Get roommates for a student (same room, active status, excluding the student)
     */
    @Query("SELECT ra FROM RoomAllocation ra " +
           "WHERE ra.room = (SELECT ra2.room FROM RoomAllocation ra2 WHERE ra2.student = :student AND ra2.status = 'active') " +
           "AND ra.student != :student " +
           "AND ra.status = 'active'")
    List<RoomAllocation> findRoommatesForStudent(Student student);
    
    /**
     * Get allocation statistics
     */
    @Query("SELECT COUNT(ra) FROM RoomAllocation ra WHERE ra.status = 'active'")
    Long getActiveAllocationCount();
    
    /**
     * Get average compatibility score
     */
    @Query("SELECT AVG(ra.compatibilityScore) FROM RoomAllocation ra WHERE ra.status = 'active' AND ra.compatibilityScore IS NOT NULL")
    Double getAverageCompatibilityScore();
}