package com.pos.controller;

import com.pos.model.Employee;
import com.pos.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Only ADMIN can add an employee
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }
    // Only ADMIN can update an employee
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/update/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }
    // Only ADMIN can delete an employee
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully!";
    }
    // Any authenticated user (ADMIN or EMPLOYEE) can view employees
    @GetMapping("/list")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}