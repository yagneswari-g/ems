package com.example.ems.repository;

import com.example.ems.model.Employee;
import com.example.ems.model.EmploymentStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email, Sort sort);
    List<Employee> findByDepartmentId(Long departmentId, Sort sort);
    List<Employee> findByStatus(EmploymentStatus status, Sort sort);
    long countByStatus(EmploymentStatus status);
    long countByDepartmentId(Long departmentId);
}
