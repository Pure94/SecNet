package com.larpologic.secretnetwork.security.config;

import com.larpologic.secretnetwork.security.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    CustomLoginSuccessHandler customLoginSuccessHandler;

    public WebSecurityConfig( CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
                        .requestMatchers("/", "/login", "/badania", "/unauthorized").permitAll()
                        .requestMatchers("/badania/**").permitAll()
                        .requestMatchers("/admin-panel").hasRole("ADMIN")
                        .requestMatchers("/admin-panel/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .rememberMe((rememberMe) -> rememberMe
                        .key("BUNKIER_NETWORK")
                        .tokenValiditySeconds(86400 * 1)
                )
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/unauthorized")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}