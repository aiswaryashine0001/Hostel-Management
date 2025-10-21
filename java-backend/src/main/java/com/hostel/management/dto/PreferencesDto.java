package com.hostel.management.dto;

import com.hostel.management.entity.StudentPreferences;
import java.time.LocalDateTime;

/**
 * DTO for Student Preferences without circular references
 */
public class PreferencesDto {
    private Long id;
    private String sleepTime;
    private String wakeTime;
    private String studyPreference;
    private String noiseTolerance;
    private String cleanlinessLevel;
    private String socialPreference;
    private String musicPreference;
    private String visitorFrequency;
    private String temperaturePreference;
    private String dietaryPreferences;
    private String interests;
    private String additionalNotes;
    private LocalDateTime createdAt;

    // Default constructor
    public PreferencesDto() {}

    // Constructor from StudentPreferences entity
    public PreferencesDto(StudentPreferences preferences) {
        if (preferences != null) {
            this.id = preferences.getId();
            this.sleepTime = preferences.getSleepTime();
            this.wakeTime = preferences.getWakeTime();
            this.studyPreference = preferences.getStudyPreference();
            this.noiseTolerance = preferences.getNoiseTolerance();
            this.cleanlinessLevel = preferences.getCleanlinessLevel();
            this.socialPreference = preferences.getSocialPreference();
            this.musicPreference = preferences.getMusicPreference();
            this.visitorFrequency = preferences.getVisitorFrequency();
            this.temperaturePreference = preferences.getTemperaturePreference();
            this.dietaryPreferences = preferences.getDietaryPreferences();
            this.interests = preferences.getInterests();
            this.additionalNotes = preferences.getAdditionalNotes();
            this.createdAt = preferences.getCreatedAt();
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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