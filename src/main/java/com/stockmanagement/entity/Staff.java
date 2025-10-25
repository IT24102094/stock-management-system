package com.stockmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    @Pattern(regexp = "^EMP\\d{3,}$", message = "Employee ID must start with 'EMP' followed by at least 3 digits (e.g., EMP001)")
    private String employeeId;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters")
    private String lastName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "Role is required")
    @Size(min = 2, max = 50, message = "Role must be between 2 and 50 characters")
    private String role;

    @Size(max = 50, message = "Department name must not exceed 50 characters")
    private String department;

    @Column(name = "hire_date", nullable = false)
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private Double salary;

    @Column(name = "shift_schedule")
    @Size(max = 100, message = "Shift schedule must not exceed 100 characters")
    private String shiftSchedule;

    @Column(name = "performance_rating")
    @DecimalMin(value = "1.0", message = "Performance rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Performance rating must not exceed 5.0")
    private Double performanceRating;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // NEW: Photo fields
    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "photo_thumbnail_url")
    private String photoThumbnailUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Staff() {}

    public Staff(String employeeId, String firstName, String lastName, String email,
                 String role, LocalDate hireDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
        this.isActive = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public String getShiftSchedule() { return shiftSchedule; }
    public void setShiftSchedule(String shiftSchedule) { this.shiftSchedule = shiftSchedule; }

    public Double getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(Double performanceRating) { this.performanceRating = performanceRating; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // NEW: Photo getters and setters
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhotoThumbnailUrl() { return photoThumbnailUrl; }
    public void setPhotoThumbnailUrl(String photoThumbnailUrl) { this.photoThumbnailUrl = photoThumbnailUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}