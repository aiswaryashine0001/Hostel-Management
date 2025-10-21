package com.hostel.management.repository;

import com.hostel.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Student entity operations
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by student ID
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * Find student by email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Check if student exists by student ID
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * Check if student exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find students with preferences but no room allocation
     */
    @Query("SELECT s FROM Student s " +
        "WHERE EXISTS (SELECT p FROM StudentPreferences p WHERE p.student = s) " +
        "AND s.roomAllocation IS NULL")
    List<Student> findStudentsWithPreferencesButNoAllocation();
    
    /**
     * Find students by course
     */
    List<Student> findByCourse(String course);
    
    /**
     * Find students by year
     */
    List<Student> findByYear(Integer year);
    
    /**
     * Find students by gender
     */
    List<Student> findByGender(String gender);
    
    /**
     * Find allocated students
     */
    @Query("SELECT s FROM Student s WHERE s.roomAllocation IS NOT NULL")
    List<Student> findAllocatedStudents();
    
    /**
     * Find unallocated students
     */
    @Query("SELECT s FROM Student s WHERE s.roomAllocation IS NULL")
    List<Student> findUnallocatedStudents();
}