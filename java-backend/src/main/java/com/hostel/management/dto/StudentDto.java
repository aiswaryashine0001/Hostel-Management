package com.hostel.management.dto;

import com.hostel.management.entity.Student;
import java.time.LocalDateTime;

/**
 * Simple DTO for Student data transfer without circular references
 */
public class StudentDto {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String course;
    private Integer year;
    private String gender;
    private LocalDateTime createdAt;
    private PreferencesDto preferences;

    // Default constructor
    public StudentDto() {}

    // Constructor from Student entity
    public StudentDto(Student student) {
        this.id = student.getId();
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.phone = student.getPhone();
        this.course = student.getCourse();
        this.year = student.getYear();
        this.gender = student.getGender();
        this.createdAt = student.getCreatedAt();
        this.preferences = student.getPreferences() != null ? new PreferencesDto(student.getPreferences()) : null;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PreferencesDto getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesDto preferences) {
        this.preferences = preferences;
    }
}