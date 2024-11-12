package com.HrManager.HrManagerSystem.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.HrManager.HrManagerSystem.Service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

        // Cấu hình DaoAuthenticationProvider để sử dụng UserDetailsService và
        // PasswordEncoder
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        // Cấu hình AuthenticationManager
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .authenticationProvider(authenticationProvider())
                                .build();
        }

        @SuppressWarnings("deprecation")
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeRequests(requests -> requests
                                                .requestMatchers("/register", "/login", "/verify", "/css/**",
                                                                "/js/**",
                                                                "/assets/demo/**",
                                                                "/homepage")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .failureHandler(customAuthenticationFailureHandler) // Ð? x? l? l?i ðãng
                                                                                                    // nh?p
                                                .successHandler((request, response, authentication) -> {
                                                        String role = authentication.getAuthorities().stream()
                                                                        .map(GrantedAuthority::getAuthority)
                                                                        .findFirst()
                                                                        .orElse("ROLE_USER");

                                                        if (role.equals("ROLE_ADMIN")) {
                                                                response.sendRedirect("/admin/index");
                                                        } else if (role.equals("ROLE_EMPLOYEE")) {
                                                                response.sendRedirect("/employee/homepage");
                                                        } else {
                                                                response.sendRedirect("/homepage"); // Default redirect
                                                        }
                                                })
                                                .permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
