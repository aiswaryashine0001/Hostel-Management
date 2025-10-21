package com.hostel.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Room Allocation entity representing the assignment of students to rooms
 */
@Entity
@Table(name = "room_allocations")
public class RoomAllocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private Room room;
    
    @CreationTimestamp
    @Column(name = "allocation_date", updatable = false)
    private LocalDateTime allocationDate;
    
    @Column(nullable = false)
    private String status = "active";
    
    @Column(name = "compatibility_score")
    private Double compatibilityScore;
    
    // Default constructor
    public RoomAllocation() {}
    
    // Constructor with required fields
    public RoomAllocation(Student student, Room room, Double compatibilityScore) {
        this.student = student;
        this.room = room;
        this.compatibilityScore = compatibilityScore;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public LocalDateTime getAllocationDate() {
        return allocationDate;
    }
    
    public void setAllocationDate(LocalDateTime allocationDate) {
        this.allocationDate = allocationDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getCompatibilityScore() {
        return compatibilityScore;
    }
    
    public void setCompatibilityScore(Double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }
    
    @Override
    public String toString() {
        return "RoomAllocation{" +
                "id=" + id +
                ", student=" + (student != null ? student.getName() : "null") +
                ", room=" + (room != null ? room.getRoomNumber() : "null") +
                ", allocationDate=" + allocationDate +
                ", status='" + status + '\'' +
                ", compatibilityScore=" + compatibilityScore +
                '}';
    }
}