package com.example.lms.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String memberId;
    private String email;
    @Column(nullable = false)
    private String password;
    private String fatherName;
    private String contactNumber;
    private String address;
    @Enumerated(EnumType.STRING) 
    private Gender gender;
    private LocalDateTime updated = LocalDateTime.now();
    private String creater;
    private String updater;

    @Column(updatable = false)
    private LocalDateTime created;
    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }
    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {}

    public Member(String name, String memberId, String email, String password, String fatherName, String contactNumber, String address, Gender gender, LocalDateTime created, LocalDateTime updated, String creater, String updater) {
        this.name = name;
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.fatherName = fatherName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.gender = gender;
        this.created = created;
        this.updated = updated;
        this.creater = creater;
        this.updater = updater;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedAt() {
        return created;
    }
    public void setCreatedAt(LocalDateTime created) {
        this.created = created;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getFatherName() {
        return fatherName;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public String getAddress() {
        return address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    public String getCreater() {
        return creater;
    }
    public void setCreater(String creater) {
        this.creater = creater;
    }
    public String getUpdater() {
        return updater;
    }
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
