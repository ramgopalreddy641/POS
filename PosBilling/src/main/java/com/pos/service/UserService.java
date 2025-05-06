package com.pos.service;

import com.pos.model.User;
import com.pos.repo.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
  
    @Autowired
    private PasswordEncoder passwordEncoder;
  
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

	 public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
