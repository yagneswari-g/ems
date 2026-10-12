package com.example.ems.controller;

import com.example.ems.dto.DepartmentDtos.*;
import com.example.ems.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departments;
    public DepartmentController(DepartmentService departments) { this.departments = departments; }
    @GetMapping public List<DepartmentResponse> all(@RequestHeader(value = "X-Auth-Token", required = false) String token) { return departments.all(token); }
    @PostMapping public DepartmentResponse create(@RequestHeader(value = "X-Auth-Token", required = false) String token, @Valid @RequestBody DepartmentRequest request) { return departments.create(token, request); }
    @PutMapping("/{id}") public DepartmentResponse update(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long id, @Valid @RequestBody DepartmentRequest request) { return departments.update(token, id, request); }
    @DeleteMapping("/{id}") public void delete(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long id) { departments.delete(token, id); }
}
