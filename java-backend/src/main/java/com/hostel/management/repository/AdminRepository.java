package com.hostel.management.repository;

import com.hostel.management.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Admin entity operations
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    /**
     * Find admin by username
     */
    Optional<Admin> findByUsername(String username);
    
    /**
     * Check if admin exists by username
     */
    boolean existsByUsername(String username);
    
    /**
     * Find admin by email
     */
    Optional<Admin> findByEmail(String email);
}