package com.hostel.management.repository;

import com.hostel.management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Room entity operations
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * Find room by room number
     */
    Optional<Room> findByRoomNumber(String roomNumber);
    
    /**
     * Check if room exists by room number
     */
    boolean existsByRoomNumber(String roomNumber);
    
    /**
     * Find available rooms (not at full capacity)
     */
    @Query("SELECT r FROM Room r WHERE r.occupied < r.capacity AND r.status = 'available'")
    List<Room> findAvailableRooms();
    
    /**
     * Find rooms by building
     */
    List<Room> findByBuilding(String building);
    
    /**
     * Find rooms by floor
     */
    List<Room> findByFloor(Integer floor);
    
    /**
     * Find rooms by capacity
     */
    List<Room> findByCapacity(Integer capacity);
    
    /**
     * Find rooms by status
     */
    List<Room> findByStatus(String status);
    
    /**
     * Find rooms with specific available spaces
     */
    @Query("SELECT r FROM Room r WHERE (r.capacity - r.occupied) >= :spaces")
    List<Room> findRoomsWithAvailableSpaces(Integer spaces);
    
    /**
     * Get total room count
     */
    @Query("SELECT COUNT(r) FROM Room r")
    Long getTotalRoomCount();
    
    /**
     * Get occupied room count
     */
    @Query("SELECT COUNT(r) FROM Room r WHERE r.occupied > 0")
    Long getOccupiedRoomCount();
    
    /**
     * Get available room count
     */
    @Query("SELECT COUNT(r) FROM Room r WHERE r.occupied < r.capacity AND r.status = 'available'")
    Long getAvailableRoomCount();
}