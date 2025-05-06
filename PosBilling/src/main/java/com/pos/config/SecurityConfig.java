package com.pos.config;

import com.pos.config.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public Access
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .requestMatchers("/api/invoices/get").permitAll() // Allow invoice retrieval for all roles

                // Admin & Manager: Full access
                .requestMatchers("/api/employees/**", "/api/products/**", "/api/orders/**", "/api/invoices/**")
                .hasAnyAuthority("ROLE_Admin", "ROLE_Manager")

                // Cashier: Limited access (Orders, Invoices, Products)
                .requestMatchers("/api/orders/**", "/api/invoices/**", "/api/products/")
                .hasAuthority("ROLE_Cashier")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .formLogin(login -> login
                .loginProcessingUrl("/api/users/login")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Login successful\"}");
                })
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/users/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Logout successful\"}");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid username or password\"}");
        };
    }

    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Employee-Management");
        return entryPoint;
    }
}



































//package com.pos.config;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//
//@Configuration
//public class SecurityConfig {
//
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(customUserDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
//                .requestMatchers("/api/users/logout").authenticated()
//
//                // Admin: Full access
//                .requestMatchers("/api/employees/**", "/api/invoices/**", "/api/orders/**", "/api/products/**")
//                .hasAuthority("ROLE_Admin")
//
//                // Manager: Can manage employees and all resources
//                .requestMatchers("/api/employees/**", "/api/invoices/**", "/api/orders/**", "/api/products/**")
//                .hasAuthority("ROLE_Manager")
//
//                // Cashier: Limited to invoices, orders, and products
//                .requestMatchers("/api/invoices/**", "/api/orders/**", "/api/products/**")
//                .hasAuthority("ROLE_Cashier")
//
//                // Any other requests require authentication
//                .anyRequest().authenticated()
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//            .formLogin(login -> login
//                .loginProcessingUrl("/api/users/login")
//                .successHandler((request, response, authentication) -> {
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"message\": \"Login successful\"}");
//                })
//                .failureHandler(authenticationFailureHandler())
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/api/users/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"message\": \"Logout successful\"}");
//                })
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return (request, response, exception) -> {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"message\": \"Invalid username or password\"}");
//        };
//    }
//
//    @Bean
//    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
//        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
//        entryPoint.setRealmName("Employee-Management");
//        return entryPoint;
//    }
//}



















//package com.pos.config;
//
//import com.pos.security.CustomUserDetailsService;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//
//@Configuration
//public class SecurityConfig {
//
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(customUserDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
//                .requestMatchers("/api/employees/**", "/api/invoices/**", "/api/orders/**", "/api/products/**").hasRole("Admin")
//                .requestMatchers("/api/invoices/**", "/api/orders/**", "/api/products").hasRole("Cashier") 
//                .requestMatchers("/api/employees/**", "/api/invoices/**", "/api/orders/**", "/api/products/**").hasRole("Manager")
//                .requestMatchers("/api/users/logout").authenticated()
//                .anyRequest().authenticated()
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//            .formLogin(login -> login
//                .loginProcessingUrl("/api/users/login")
//                .successHandler((request, response, authentication) -> {
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"Login successful\"}");
//                })
//                .failureHandler(authenticationFailureHandler()) // Handle failed login attempts
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/api/users/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"Logout successful\"}");
//                })
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return (request, response, exception) -> {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"Invalid username or password\"}");
//        };
//    }
//
//    @Bean
//    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
//        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
//        entryPoint.setRealmName("Employee-Management");
//        return entryPoint;
//    }
//}








//package com.pos.config;
//
//import com.pos.security.CustomUserDetailsService;
//import com.pos.security.EmployeeUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//
//@Configuration
//public class SecurityConfig {
//
//    private final CustomUserDetailsService customUserDetailsService;
//	public SecurityConfig(CustomUserDetailsService customUserDetailsService, EmployeeUserDetailsService employeeUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(customUserDetailsService); // Handles both users and employees
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) 
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
//                .requestMatchers("/api/employees/**").hasRole("Admin")
//                .requestMatchers("/api/users/logout").authenticated()
//                .anyRequest().authenticated()
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Enable session authentication
//            .formLogin(login -> login
//                .loginProcessingUrl("/api/users/login")
//                .successHandler((request, response, authentication) -> {
//                    response.getWriter().write("{\"message\": \"Login successful\"}");
//                })
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/api/users/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//                    response.getWriter().write("{\"message\": \"Logout successful\"}");
//                })
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//            );
//
//        return http.build();
//    }   
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////            .csrf(csrf -> csrf.disable())
////            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////            .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint()))
////            .authorizeHttpRequests(auth -> auth
////                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
////                .requestMatchers("/api/employees/**").hasRole("Admin")
////                .requestMatchers("/api/users/logout").authenticated()
////                .anyRequest().authenticated()
////            );
////
////        return http.build();
////    }
//
//    @Bean
//    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
//        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
//        entryPoint.setRealmName("Employee-Management");
//        return entryPoint;
//    }
//}
