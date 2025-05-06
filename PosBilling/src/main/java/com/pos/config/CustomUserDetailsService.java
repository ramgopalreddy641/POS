package com.pos.config;

import com.pos.model.User;
import com.pos.model.Employee;
import com.pos.repo.UserRepository;
import com.pos.repo.EmployeeRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try finding the user in the Users table first
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }

        // If not found in Users, try Employees table
        Optional<Employee> employeeOpt = employeeRepository.findByUsername(username);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
            );
        }

        // If username is not found in either table, throw exception
        throw new UsernameNotFoundException("User or Employee not found: " + username);
    }
}







































//package com.pos.config;
//
//import com.pos.model.User;
//import com.pos.model.Employee;
//import com.pos.repo.UserRepository;
//import com.pos.repo.EmployeeRepository;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    private final EmployeeRepository employeeRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository) {
//        this.userRepository = userRepository;
//        this.employeeRepository = employeeRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Check in Users table first
//        User user = userRepository.findByUsername(username).orElse(null);
//        if (user != null) {
//            return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
//            );
//        }
//        // Check in Employees table if not found in Users
//        Employee employee = employeeRepository.findByUsername(username).orElseThrow(() -> 
//            new UsernameNotFoundException("User or Employee not found: " + username));
//
//        return new org.springframework.security.core.userdetails.User(
//            employee.getUsername(),
//            employee.getPassword(),
//            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
//        );
//    }
//}
