package com.pos.controller;

import com.pos.model.User;
import com.pos.dto.LoginRequest;
import com.pos.dto.RegisterRequest;
import com.pos.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired 
    PasswordEncoder passwordEncoder;
  
    @Autowired
    private AuthenticationManager authenticationManager;
  
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());
        userService.registerUser(newUser);
        return ResponseEntity.ok("Admin registered successfully");
    }
  
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> existingUser = userService.findByUsername(request.getUsername()); // Fetch user from DB

        if (existingUser.isEmpty() || !passwordEncoder.matches(request.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password!"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User loggedInUser = existingUser.get();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("username", loggedInUser.getUsername());
        response.put("role", loggedInUser.getRole().name());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Clear authentication context
        SecurityContextHolder.clearContext();

        // Send logout confirmation
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful!");
        return ResponseEntity.ok(responseBody);
    }
}

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
//        try {
//            UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
//            Authentication auth = authenticationManager.authenticate(authToken);
//            // Set the authentication in the security context
//            SecurityContextHolder.getContext().setAuthentication(auth);
//            return ResponseEntity.ok("Login successful");
//        } catch (AuthenticationException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//    }
//  
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//            SecurityContextHolder.clearContext();
//        }
//        response.setHeader("WWW-Authenticate", "Basic realm=\"Employee-Management\"");
//        return ResponseEntity.ok("Logout successful");
//    }
//}