package com.hostel.management.entity;

import jakarta.persistence.*;

import java.util.List;

/**
 * Room entity representing a hostel room
 */
@Entity
@Table(name = "rooms")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;
    
    @Column(nullable = false)
    private Integer capacity = 2;
    
    @Column(nullable = false)
    private Integer occupied = 0;
    
    private Integer floor;
    
    private String building;
    
    @Column(columnDefinition = "TEXT")
    private String amenities;
    
    @Column(nullable = false)
    private String status = "available";
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomAllocation> allocations;
    
    // Default constructor
    public Room() {}
    
    // Constructor with required fields
    public Room(String roomNumber, Integer capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Integer getOccupied() {
        return occupied;
    }
    
    public void setOccupied(Integer occupied) {
        this.occupied = occupied;
    }
    
    public Integer getFloor() {
        return floor;
    }
    
    public void setFloor(Integer floor) {
        this.floor = floor;
    }
    
    public String getBuilding() {
        return building;
    }
    
    public void setBuilding(String building) {
        this.building = building;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<RoomAllocation> getAllocations() {
        return allocations;
    }
    
    public void setAllocations(List<RoomAllocation> allocations) {
        this.allocations = allocations;
    }
    
    // Utility methods
    public boolean isAvailable() {
        return occupied < capacity && "available".equals(status);
    }
    
    public int getAvailableSpaces() {
        return capacity - occupied;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", capacity=" + capacity +
                ", occupied=" + occupied +
                ", building='" + building + '\'' +
                ", floor=" + floor +
                ", status='" + status + '\'' +
                '}';
    }
}