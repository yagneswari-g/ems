package com.example.ems.controller;

import com.example.ems.dto.EmployeeDtos.*;
import com.example.ems.model.EmploymentStatus;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employees;
    public EmployeeController(EmployeeService employees) { this.employees = employees; }
    @GetMapping public List<EmployeeResponse> all(@RequestHeader(value = "X-Auth-Token", required = false) String token, @RequestParam(required = false) String keyword, @RequestParam(required = false) Long departmentId, @RequestParam(required = false) EmploymentStatus status) { return employees.all(token, keyword, departmentId, status); }
    @GetMapping("/{id}") public EmployeeResponse one(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long id) { return employees.one(token, id); }
    @PostMapping public ResponseEntity<EmployeeResponse> create(@RequestHeader(value = "X-Auth-Token", required = false) String token, @Valid @RequestBody EmployeeRequest request) { EmployeeResponse response = employees.create(token, request); return ResponseEntity.created(URI.create("/api/employees/" + response.id())).body(response); }
    @PutMapping("/{id}") public EmployeeResponse update(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long id, @Valid @RequestBody EmployeeRequest request) { return employees.update(token, id, request); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long id) { employees.delete(token, id); return ResponseEntity.noContent().build(); }
}
