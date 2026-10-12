package com.example.ems.service;

import com.example.ems.dto.EmployeeDtos.*;
import com.example.ems.exception.ApiExceptions;
import com.example.ems.model.Employee;
import com.example.ems.model.EmploymentStatus;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employees;
    private final DepartmentService departments;
    private final AuthService auth;
    private final NotificationService notifications;

    public EmployeeService(EmployeeRepository employees, DepartmentService departments, AuthService auth, NotificationService notifications) {
        this.employees = employees; this.departments = departments; this.auth = auth; this.notifications = notifications;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> all(String token, String keyword, Long departmentId, EmploymentStatus status) {
        auth.requireUser(token);
        Sort sort = Sort.by(Sort.Order.asc("lastName"), Sort.Order.asc("firstName"));
        List<Employee> result;
        if (keyword != null && !keyword.isBlank()) result = employees.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, keyword, sort);
        else if (departmentId != null) result = employees.findByDepartmentId(departmentId, sort);
        else if (status != null) result = employees.findByStatus(status, sort);
        else result = employees.findAll(sort);
        if (departmentId != null && keyword != null && !keyword.isBlank()) result = result.stream().filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(departmentId)).toList();
        if (status != null && (keyword != null && !keyword.isBlank() || departmentId != null)) result = result.stream().filter(e -> e.getStatus() == status).toList();
        return result.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse one(String token, Long id) { auth.requireUser(token); return toResponse(get(id)); }

    @Transactional
    public EmployeeResponse create(String token, EmployeeRequest request) {
        auth.requireManager(token);
        if (employees.existsByEmailIgnoreCase(request.email())) throw new ApiExceptions.BadRequest("Employee email already exists");
        Employee e = new Employee(); copy(request, e); employees.save(e);
        notifications.send(e.getEmail(), "Employee record created", "Your employee record was created in EmployeeHub.");
        return toResponse(e);
    }

    @Transactional
    public EmployeeResponse update(String token, Long id, EmployeeRequest request) {
        auth.requireManager(token); Employee e = get(id);
        if (employees.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) throw new ApiExceptions.BadRequest("Employee email already exists");
        copy(request, e); employees.save(e);
        notifications.send(e.getEmail(), "Employee record updated", "Your employee record was updated in EmployeeHub.");
        return toResponse(e);
    }

    @Transactional
    public void delete(String token, Long id) {
        auth.requireManager(token); Employee e = get(id); employees.delete(e);
        notifications.send(e.getEmail(), "Employee record removed", "Your employee record was removed from EmployeeHub.");
    }

    @Transactional
    public EmployeeResponse updateProfileImage(String token, Long id, String storedPath) {
        auth.requireManager(token); Employee e = get(id); e.setProfileImagePath(storedPath); employees.save(e); return toResponse(e);
    }

    public Employee get(Long id) { return employees.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Employee not found")); }

    public EmployeeResponse toResponse(Employee e) {
        Long departmentId = e.getDepartment() == null ? null : e.getDepartment().getId();
        String departmentName = e.getDepartment() == null ? "Unassigned" : e.getDepartment().getName();
        String image = e.getProfileImagePath() == null ? null : "/api/files/profile/" + e.getId();
        return new EmployeeResponse(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getPhone(), e.getGender(), e.getDateOfBirth(), e.getAddress(), departmentId, departmentName, e.getDesignation(), e.getSalary(), e.getJoiningDate(), e.getStatus(), image, e.getCreatedAt(), e.getUpdatedAt());
    }

    private void copy(EmployeeRequest r, Employee e) {
        e.setFirstName(r.firstName().trim()); e.setLastName(r.lastName().trim()); e.setEmail(r.email().trim().toLowerCase());
        e.setPhone(r.phone()); e.setGender(r.gender()); e.setDateOfBirth(r.dateOfBirth()); e.setAddress(r.address());
        e.setDepartment(r.departmentId() == null ? null : departments.get(r.departmentId())); e.setDesignation(r.designation());
        e.setSalary(r.salary()); e.setJoiningDate(r.joiningDate()); e.setStatus(r.status() == null ? EmploymentStatus.ACTIVE : r.status());
    }
}
