package com.example.ems.dto;

import com.example.ems.model.EmploymentStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class EmployeeDtos {
    private EmployeeDtos() {}
    public record EmployeeRequest(
            @NotBlank String firstName, @NotBlank String lastName, @Email @NotBlank String email,
            String phone, String gender, LocalDate dateOfBirth, String address, Long departmentId,
            String designation, @DecimalMin("0.0") BigDecimal salary, LocalDate joiningDate,
            EmploymentStatus status) {}
    public record EmployeeResponse(
            Long id, String firstName, String lastName, String email, String phone, String gender,
            LocalDate dateOfBirth, String address, Long departmentId, String departmentName,
            String designation, BigDecimal salary, LocalDate joiningDate, EmploymentStatus status,
            String profileImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {}
    public record DocumentResponse(Long id, String originalFileName, String contentType, long size,
                                   LocalDateTime uploadedAt, String downloadUrl) {}
    public record DashboardResponse(long totalEmployees, long activeEmployees, long inactiveEmployees,
                                    long onLeaveEmployees, long totalDepartments) {}
}
