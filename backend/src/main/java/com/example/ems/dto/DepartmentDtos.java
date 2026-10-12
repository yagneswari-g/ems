package com.example.ems.dto;

import jakarta.validation.constraints.NotBlank;

public final class DepartmentDtos {
    private DepartmentDtos() {}
    public record DepartmentRequest(@NotBlank String name, String description) {}
    public record DepartmentResponse(Long id, String name, String description, long employeeCount) {}
}
