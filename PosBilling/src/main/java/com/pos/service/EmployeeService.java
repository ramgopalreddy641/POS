package com.pos.service;

import com.pos.model.Employee;
import com.pos.model.EmployeeRole;
import com.pos.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
  
    @Autowired
    private PasswordEncoder passwordEncoder;
  
    public Employee createEmployee(Employee employee) {
        if(employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Duplicate employee email found!");
        }
        // Generate custom employeeId based on role
        String prefix = getPrefix(employee.getRole());
        String newEmployeeId = generateEmployeeId(prefix);
        employee.setEmployeeId(newEmployeeId);
      
        // Encode the employee password using BCrypt
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
      
        if(employee.getStatus() == null) {
            employee.setStatus(true);
        }
        return employeeRepository.save(employee);
    }
  
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
  
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
  
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
      
        if(!employee.getEmail().equals(updatedEmployee.getEmail())) {
            if(employeeRepository.existsByEmail(updatedEmployee.getEmail())) {
                throw new RuntimeException("Another employee with this email already exists!");
            }
            employee.setEmail(updatedEmployee.getEmail());
        }
      
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setMobileNo(updatedEmployee.getMobileNo());
        employee.setAddress(updatedEmployee.getAddress());
        employee.setBranchCode(updatedEmployee.getBranchCode());
        employee.setBranchLocation(updatedEmployee.getBranchLocation());
      
        if(employee.getRole() != updatedEmployee.getRole()) {
            employee.setRole(updatedEmployee.getRole());
            String prefix = getPrefix(updatedEmployee.getRole());
            String newEmployeeId = generateEmployeeId(prefix);
            employee.setEmployeeId(newEmployeeId);
        }
      
        return employeeRepository.save(employee);
    }
  
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus(false);
        employeeRepository.save(employee);
    }
  
    private String getPrefix(EmployeeRole role) {
        switch(role) {
            case Cashier: return "CASH-";
            case Manager: return "MANG-";
            default: return "EMP-";
        }
    }
    private String generateEmployeeId(String prefix) {
        Optional<Employee> lastEmployeeOpt = employeeRepository.findTopByEmployeeIdStartingWithOrderByEmployeeIdDesc(prefix);
        int number = 1;
        if(lastEmployeeOpt.isPresent()){
            String lastId = lastEmployeeOpt.get().getEmployeeId();
            String numericPart = lastId.substring(prefix.length());
            try {
                number = Integer.parseInt(numericPart) + 1;
            } catch(NumberFormatException e) {
                number = 1;
            }
        }
        DecimalFormat df = new DecimalFormat("000");
        return prefix + df.format(number);
    }
}
