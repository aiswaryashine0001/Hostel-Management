package com.hostel.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Student Preferences entity for storing roommate preferences
 */
@Entity
@Table(name = "student_preferences")
public class StudentPreferences {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(name = "sleep_time")
    private String sleepTime;
    
    @Column(name = "wake_time")
    private String wakeTime;
    
    @Column(name = "study_preference")
    private String studyPreference;
    
    @Column(name = "noise_tolerance")
    private String noiseTolerance;
    
    @Column(name = "cleanliness_level")
    private String cleanlinessLevel;
    
    @Column(name = "social_preference")
    private String socialPreference;
    
    @Column(name = "music_preference")
    private String musicPreference;
    
    @Column(name = "visitor_frequency")
    private String visitorFrequency;
    
    @Column(name = "temperature_preference")
    private String temperaturePreference;
    
    @Column(name = "dietary_preferences")
    private String dietaryPreferences;
    
    @Column(columnDefinition = "TEXT")
    private String interests;
    
    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Default constructor
    public StudentPreferences() {}
    
    // Constructor with student
    public StudentPreferences(Student student) {
        this.student = student;
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
    
    public String getSleepTime() {
        return sleepTime;
    }
    
    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }
    
    public String getWakeTime() {
        return wakeTime;
    }
    
    public void setWakeTime(String wakeTime) {
        this.wakeTime = wakeTime;
    }
    
    public String getStudyPreference() {
        return studyPreference;
    }
    
    public void setStudyPreference(String studyPreference) {
        this.studyPreference = studyPreference;
    }
    
    public String getNoiseTolerance() {
        return noiseTolerance;
    }
    
    public void setNoiseTolerance(String noiseTolerance) {
        this.noiseTolerance = noiseTolerance;
    }
    
    public String getCleanlinessLevel() {
        return cleanlinessLevel;
    }
    
    public void setCleanlinessLevel(String cleanlinessLevel) {
        this.cleanlinessLevel = cleanlinessLevel;
    }
    
    public String getSocialPreference() {
        return socialPreference;
    }
    
    public void setSocialPreference(String socialPreference) {
        this.socialPreference = socialPreference;
    }
    
    public String getMusicPreference() {
        return musicPreference;
    }
    
    public void setMusicPreference(String musicPreference) {
        this.musicPreference = musicPreference;
    }
    
    public String getVisitorFrequency() {
        return visitorFrequency;
    }
    
    public void setVisitorFrequency(String visitorFrequency) {
        this.visitorFrequency = visitorFrequency;
    }
    
    public String getTemperaturePreference() {
        return temperaturePreference;
    }
    
    public void setTemperaturePreference(String temperaturePreference) {
        this.temperaturePreference = temperaturePreference;
    }
    
    public String getDietaryPreferences() {
        return dietaryPreferences;
    }
    
    public void setDietaryPreferences(String dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public String getAdditionalNotes() {
        return additionalNotes;
    }
    
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}