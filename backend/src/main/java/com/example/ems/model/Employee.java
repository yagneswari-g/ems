package com.example.ems.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 60) private String firstName;
    @Column(nullable = false, length = 60) private String lastName;
    @Column(nullable = false, unique = true, length = 120) private String email;
    @Column(length = 30) private String phone;
    @Column(length = 20) private String gender;
    private LocalDate dateOfBirth;
    @Column(length = 500) private String address;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "department_id") private Department department;
    @Column(length = 80) private String designation;
    @Column(precision = 12, scale = 2) private BigDecimal salary;
    private LocalDate joiningDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private EmploymentStatus status = EmploymentStatus.ACTIVE;
    @Column(length = 500) private String profileImagePath;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate public void updateTimestamp() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String value) { firstName = value; }
    public String getLastName() { return lastName; }
    public void setLastName(String value) { lastName = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getGender() { return gender; }
    public void setGender(String value) { gender = value; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate value) { dateOfBirth = value; }
    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department value) { department = value; }
    public String getDesignation() { return designation; }
    public void setDesignation(String value) { designation = value; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal value) { salary = value; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate value) { joiningDate = value; }
    public EmploymentStatus getStatus() { return status; }
    public void setStatus(EmploymentStatus value) { status = value; }
    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String value) { profileImagePath = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
