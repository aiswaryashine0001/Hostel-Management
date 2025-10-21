package com.hostel.management.repository;

import com.hostel.management.entity.StudentPreferences;
import com.hostel.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for StudentPreferences entity operations
 */
@Repository
public interface StudentPreferencesRepository extends JpaRepository<StudentPreferences, Long> {
    
    /**
     * Find preferences by student
     */
    Optional<StudentPreferences> findByStudent(Student student);
    
    /**
     * Find preferences by student ID
     */
    Optional<StudentPreferences> findByStudentId(Long studentId);
    
    /**
     * Check if preferences exist for student
     */
    boolean existsByStudent(Student student);
    
    /**
     * Check if preferences exist for student ID
     */
    boolean existsByStudentId(Long studentId);
    
    /**
     * Delete preferences by student
     */
    void deleteByStudent(Student student);
}