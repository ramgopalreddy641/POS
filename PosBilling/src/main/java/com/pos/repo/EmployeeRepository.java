package com.pos.repo;

import com.pos.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);
  
    @Query("SELECT e FROM Employee e WHERE e.employeeId LIKE ?1% ORDER BY e.employeeId DESC")
    Optional<Employee> findTopByEmployeeIdStartingWithOrderByEmployeeIdDesc(String prefix);
  
    boolean existsByEmail(String email);
    // New: Find by employee login username
    Optional<Employee> findByUsername(String username);
  
    boolean existsByFirstNameAndLastNameAndMobileNo(String firstName, String lastName, String mobileNo);
}

