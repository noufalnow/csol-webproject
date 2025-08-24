package com.dms.kalari.config;

import com.dms.kalari.security.PrivilegeChecker;
import com.dms.kalari.security.CustomUserDetailsService;
import com.dms.kalari.security.RequestAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PrivilegeChecker privilegeChecker;
    private final RequestAuthorizationManager requestAuthorizationManager;

    public SecurityConfig(PrivilegeChecker privilegeChecker, 
                         RequestAuthorizationManager requestAuthorizationManager) {
        this.privilegeChecker = privilegeChecker;
        this.requestAuthorizationManager = requestAuthorizationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            //.csrf(csrf -> csrf.disable())
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/logout").permitAll() 	
                .requestMatchers("/public/**", "/login", "/login-error", "/verify/**", 
                               "/error", "/access-denied", "/health", "/actuator/health").permitAll()
                .requestMatchers("/files/download/*").authenticated()
                .requestMatchers("/files/view/*").authenticated()
                .anyRequest().access(requestAuthorizationManager) // Use your custom authorization manager
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/css/**", "/js/**", "/images/**", "/webjars/**", 
            "/static/**", "/assets/**", "/favicon.ico"
        );
    }

    @Bean
    public AuthenticationManager authManager(
        HttpSecurity http,
        CustomUserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/access-denied");
        };
    }
}