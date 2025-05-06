package com.pos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "employees", uniqueConstraints = {
    @UniqueConstraint(columnNames = "employeeId"),
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "username")
})
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Column(nullable = false, unique = true)
    private String employeeId;
  
    @Column(nullable = false)
    private String lastName;
  
    @Column(nullable = false)
    private String firstName;
  
    @Column(nullable = false)
    private String mobileNo;
  
    @Column(nullable = false)
    private String address;
  
    @Column(nullable = false)
    private String branchCode;
  
    @Column(nullable = false)
    private String branchLocation;
  
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;
  
    @Column(nullable = false, unique = true)
    private String email;
  
    // New fields for employee login
    @Column(nullable = false, unique = true)
    private String username;
  
    @Column(nullable = false)
    private String password;
  
    @Column(nullable = false)
    private Boolean status = true;
  
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
  
    @LastModifiedDate
    private LocalDateTime updatedAt;
  
    // Default constructor
    public Employee() {
    }
  
    // Parameterized constructor (excluding id, createdAt, updatedAt)
    public Employee(String employeeId, String lastName, String firstName, String mobileNo, String address,
                    String branchCode, String branchLocation, EmployeeRole role, String email,
                    String username, String password, Boolean status) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobileNo = mobileNo;
        this.address = address;
        this.branchCode = branchCode;
        this.branchLocation = branchLocation;
        this.role = role;
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
    }
  
    // Getters and setters for all fields

    public Long getId() {
        return id;
    }
  
    public void setId(Long id) {
        this.id = id;
    }
  
    public String getEmployeeId() {
        return employeeId;
    }
  
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
  
    public String getLastName() {
        return lastName;
    }
  
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
  
    public String getFirstName() {
        return firstName;
    }
  
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
  
    public String getMobileNo() {
        return mobileNo;
    }
  
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
  
    public String getAddress() {
        return address;
    }
  
    public void setAddress(String address) {
        this.address = address;
    }
  
    public String getBranchCode() {
        return branchCode;
    }
  
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
  
    public String getBranchLocation() {
        return branchLocation;
    }
  
    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }
  
    public EmployeeRole getRole() {
        return role;
    }
  
    public void setRole(EmployeeRole role) {
        this.role = role;
    }
  
    public String getEmail() {
        return email;
    }
  
    public void setEmail(String email) {
        this.email = email;
    }
  
    public String getUsername() {
        return username;
    }
  
    public void setUsername(String username) {
        this.username = username;
    }
  
    public String getPassword() {
        return password;
    }
  
    public void setPassword(String password) {
        this.password = password;
    }
  
    public Boolean getStatus() {
        return status;
    }
  
    public void setStatus(Boolean status) {
        this.status = status;
    }
  
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
  
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
  
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
  
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
