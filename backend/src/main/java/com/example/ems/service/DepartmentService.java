package com.example.ems.service;

import com.example.ems.dto.DepartmentDtos.*;
import com.example.ems.exception.ApiExceptions;
import com.example.ems.model.Department;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departments;
    private final EmployeeRepository employees;
    private final AuthService auth;

    public DepartmentService(DepartmentRepository departments, EmployeeRepository employees, AuthService auth) {
        this.departments = departments; this.employees = employees; this.auth = auth;
    }
    public List<DepartmentResponse> all(String token) {
        auth.requireUser(token);
        return departments.findAll().stream().map(d -> new DepartmentResponse(d.getId(), d.getName(), d.getDescription(), employees.countByDepartmentId(d.getId()))).toList();
    }
    public DepartmentResponse create(String token, DepartmentRequest request) {
        auth.requireManager(token);
        if (departments.existsByNameIgnoreCase(request.name().trim())) throw new ApiExceptions.BadRequest("Department already exists");
        Department d = new Department(); d.setName(request.name().trim()); d.setDescription(request.description()); departments.save(d);
        return new DepartmentResponse(d.getId(), d.getName(), d.getDescription(), 0);
    }
    public DepartmentResponse update(String token, Long id, DepartmentRequest request) {
        auth.requireManager(token); Department d = get(id);
        d.setName(request.name().trim()); d.setDescription(request.description()); departments.save(d);
        return new DepartmentResponse(d.getId(), d.getName(), d.getDescription(), employees.countByDepartmentId(id));
    }
    public void delete(String token, Long id) { auth.requireManager(token); departments.delete(get(id)); }
    public Department get(Long id) { return departments.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Department not found")); }
}
