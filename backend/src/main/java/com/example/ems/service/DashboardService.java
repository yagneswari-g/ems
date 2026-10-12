package com.example.ems.service;

import com.example.ems.dto.EmployeeDtos.DashboardResponse;
import com.example.ems.model.EmploymentStatus;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final EmployeeRepository employees; private final DepartmentRepository departments; private final AuthService auth;
    public DashboardService(EmployeeRepository employees, DepartmentRepository departments, AuthService auth) { this.employees = employees; this.departments = departments; this.auth = auth; }
    public DashboardResponse summary(String token) {
        auth.requireUser(token);
        return new DashboardResponse(employees.count(), employees.countByStatus(EmploymentStatus.ACTIVE), employees.countByStatus(EmploymentStatus.INACTIVE), employees.countByStatus(EmploymentStatus.ON_LEAVE), departments.count());
    }
}
